package com.exe.persai.service;

import com.exe.persai.constants.MessageConstants;
import com.exe.persai.model.entity.*;
import com.exe.persai.model.enums.PaidType;
import com.exe.persai.model.enums.SubscriptionType;
import com.exe.persai.model.enums.UpgradeRequestStatus;
import com.exe.persai.model.enums.UserStatus;
import com.exe.persai.model.exception.BadRequestException;
import com.exe.persai.model.exception.ResourceNotFoundException;
import com.exe.persai.model.request.*;
import com.exe.persai.model.response.GeneralResponse;
import com.exe.persai.model.response.SubscriptionResponse;
import com.exe.persai.model.response.UpgradeRequestResponse;
import com.exe.persai.model.response.UserResponse;
import com.exe.persai.repository.*;
import com.exe.persai.service.mapper.SubscriptionMapper;
import com.exe.persai.service.mapper.UpgradeRequestMapper;
import com.exe.persai.service.mapper.UserMapper;
import com.exe.persai.utils.EmailPlatform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SubscriptionService {
    @Value("${server.cron-job.secret-key}")
    private String cronJobSecretKey;
    private final int numberOfProDaysForReferredUser = 7;
    private final int numberOfProDaysForReferringUser = 15;

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final TransactionRepository transactionRepository;
    private final ReferralCodeRepository referralCodeRepository;
    private final UpgradeRequestRepository upgradeRequestRepository;
    private final EmailService emailService;
    @Qualifier("cronjobWebClient")
    private final WebClient cronjobWebClient;

    public List<SubscriptionResponse> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptions.stream().map(SubscriptionMapper.INSTANCE::toSubscriptionResponse).toList();
    }

    public SubscriptionResponse getSubscriptionById(String subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription ID not found"));
        return SubscriptionMapper.INSTANCE.toSubscriptionResponse(subscription);
    }

    public void requestToUpgradeWithPayment(PaidType paidType) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);

        if (paidType.equals(PaidType.NO))
            throw new BadRequestException("Paid type must be MONTHLY or YEARLY");

        Subscription proSubscription = subscriptionRepository.findById(SubscriptionType.PRO.toString()).get();
        BigDecimal price = paidType.equals(PaidType.MONTHLY) ? proSubscription.getPricePerMonth()
                : proSubscription.getPricePerYear();

        UpgradeRequest upgradeRequest = UpgradeRequest.builder()
                .user(currentUser)
                .paidType(paidType)
                .price(price)
                .build();
        upgradeRequest = upgradeRequestRepository.save(upgradeRequest);
    }

    public List<UpgradeRequestResponse> getAllUpgradeRequests(UpgradeRequestStatus status, Sort sort) {
        List<UpgradeRequest> upgradeRequests;
        if (status == null)
            upgradeRequests = upgradeRequestRepository.findAll(sort);
        else upgradeRequests = upgradeRequestRepository.findAllByStatus(status, sort);
        return upgradeRequests.stream().map(UpgradeRequestMapper.INSTANCE::toUpgradeRequestResponse).toList();
    }

    public UpgradeRequestResponse getUpgradeRequestById(Integer upgradeRequestId) {
        UpgradeRequest upgradeRequest = upgradeRequestRepository.findById(upgradeRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Upgrade request ID not found"));
        return UpgradeRequestMapper.INSTANCE.toUpgradeRequestResponse(upgradeRequest);
    }

    public GeneralResponse rejectUpgradeRequest(UpgradeSubscriptionRequest request) {
        UpgradeRequest upgradeRequest = upgradeRequestRepository.findById(request.getUpgradeRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Upgrade request ID not found"));
        if (upgradeRequest.getStatus().equals(UpgradeRequestStatus.SUCCEED))
            throw new BadRequestException("Request has been approved");
        upgradeRequest.setStatus(UpgradeRequestStatus.REJECT);
        return new GeneralResponse("Reject upgrade request with id " + upgradeRequest.getId() + " successfully");
    }

    public UserResponse upgradeSubscription(UpgradeSubscriptionRequest request) {
        UpgradeRequest upgradeRequest = upgradeRequestRepository.findById(request.getUpgradeRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Upgrade request ID not found"));
        if (upgradeRequest.getStatus().equals(UpgradeRequestStatus.SUCCEED))
            throw new BadRequestException("Request has been approved");
        Users user = upgradeRequest.getUser();
        if (user.getStatus().equals(UserStatus.DELETED))
            throw new BadRequestException("User has been banned. Please unbanned to upgrade user");

        Subscription proSubscription = subscriptionRepository.findById(SubscriptionType.PRO.toString()).get();
//        BigDecimal amount = upgradeRequest.getPaidType().equals(PaidType.MONTHLY)
//                ? proSubscription.getPricePerMonth() : proSubscription.getPricePerYear();

        if (user.getUserSubscription().getSubscription().getId().equals(SubscriptionType.BASIC.toString())) {
            upgradeToSpecificSubscription(upgradeRequest, proSubscription);
        }
        else {
            extendCurrentSpecificSubscription(upgradeRequest);
        }
        upgradeRequest.setStatus(UpgradeRequestStatus.SUCCEED);

        callCronjobServerToScheduleDowngradeSubscription(user.getId(), user.getUserSubscription().getExpiredDatetime());

        return UserMapper.INSTANCE.toUserResponse(user);
    }

    public Boolean downgradeSubscription(DowngradeSubscriptionRequest request) {
        log.info("Receiving request: " + request.toString());
        if (!request.getSecretKey().equals(cronJobSecretKey)) {
            log.error("Request to access API with incorrect secret key");
            return false;
        }
        Optional<UserSubscription> isUserSubscription = userSubscriptionRepository.findById(request.getUserId());
        if (isUserSubscription.isEmpty()) {
            log.error("Request with user ID not found");
            return false;
        }
        UserSubscription userSubscription = isUserSubscription.get();
        if (userSubscription.getSubscription().getId().equals(SubscriptionType.BASIC.toString()))
            return true;
        if (userSubscription.getExpiredDatetime().isAfter(Instant.now()))
            return false;
        Subscription basicSubscription = subscriptionRepository.findById(SubscriptionType.BASIC.toString()).get();
        userSubscription.setSubscription(basicSubscription);
        userSubscription.setPaidType(PaidType.NO);
        userSubscription.setExpiredDatetime(null);
        return true;
    }

    public String rescheduleDowngradeSubscription(RescheduleDowngradeRequest request) {
        if (!cronJobSecretKey.equals(request.getSecretKey()))
            throw new AccessDeniedException("Incorrect secret key to call this endpoint");

        List<UserSubscription> userSubscriptions = userSubscriptionRepository.findAllBySubscription_Id(SubscriptionType.PRO.toString());
        userSubscriptions.forEach(userSubscription -> {
            callCronjobServerToScheduleDowngradeSubscription(userSubscription.getUserId(), userSubscription.getExpiredDatetime());
        });
        return "Successfully reschedule downgrade subscriptions for: " + userSubscriptions.size() + " PRO users";
    }

    public UserResponse enterReferralCode(ReferralCodeRequest request) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        Users user = userRepository.findById(currentUser.getId()).get();

        if (user.getReferralCode().isUsingReferralCode())
            throw new BadRequestException("User has already entered referral code");
        ReferralCode otherReferralCode = referralCodeRepository.findByCodeAndUserIdNotAndUser_EnabledTrue(request.getReferralCode(), user.getId())
                .orElseThrow(() -> new BadRequestException("Referral code not found"));
        Users otherUser = otherReferralCode.getUser();

        user.getReferralCode().setUsingReferralCode(true);
        otherReferralCode.setReferenceNumber(otherReferralCode.getReferenceNumber() + 1);
        upgradeToProSubscriptionForReferralCode(user.getUserSubscription(), numberOfProDaysForReferredUser);
        upgradeToProSubscriptionForReferralCode(otherUser.getUserSubscription(), numberOfProDaysForReferringUser);

        //send mail
        emailService.send(MessageConstants.FROM_EMAIL, otherUser.getEmail(), MessageConstants.REFERRAL_EMAIL_SUBJECT,
                EmailPlatform.buildReferralEmail(user));

        //call cronjob server
        callCronjobServerToScheduleDowngradeSubscription(user.getId(), user.getUserSubscription().getExpiredDatetime());
        callCronjobServerToScheduleDowngradeSubscription(otherUser.getId(), otherUser.getUserSubscription().getExpiredDatetime());

        return UserMapper.INSTANCE.toUserResponse(user);
    }

    public void upgradeToProSubscriptionForReferralCode(UserSubscription userSubscription, int numberOfProDays) {
        Subscription proSubscription = subscriptionRepository.findById(SubscriptionType.PRO.toString()).get();
        userSubscription.setSubscription(proSubscription);
        userSubscription.setExpiredDatetime(calculateExpiredDatetime(userSubscription.getExpiredDatetime(), numberOfProDays));
    }

    private void upgradeToSpecificSubscription(UpgradeRequest upgradeRequest, Subscription subscription) {
        UserSubscription userSubscription = upgradeRequest.getUser().getUserSubscription();
        userSubscription.setSubscription(subscription);
        userSubscription.setPaidType(upgradeRequest.getPaidType());
        userSubscription.setExpiredDatetime(calculateExpiredDatetime(userSubscription.getExpiredDatetime(), upgradeRequest.getPaidType()));
        Transaction transaction = Transaction.builder()
                .user(userSubscription.getUser())
                .amount(upgradeRequest.getPrice())
                .content("Upgrade to Pro subscription with paid type: " + upgradeRequest.getPaidType().toString())
                .build();
        transaction = transactionRepository.save(transaction);
    }

    private void extendCurrentSpecificSubscription(UpgradeRequest upgradeRequest) {
        UserSubscription userSubscription = upgradeRequest.getUser().getUserSubscription();
        userSubscription.setPaidType(upgradeRequest.getPaidType());
        userSubscription.setExpiredDatetime(calculateExpiredDatetime(userSubscription.getExpiredDatetime(), upgradeRequest.getPaidType()));
        Transaction transaction = Transaction.builder()
                .user(userSubscription.getUser())
                .amount(upgradeRequest.getPrice())
                .content("Extend the current Pro subscription with paid type: " + upgradeRequest.getPaidType().toString())
                .build();
        transaction = transactionRepository.save(transaction);
    }

    private Instant calculateExpiredDatetime(Instant currentExpiredDateTime, PaidType paidType) {
        LocalDateTime baseExpiredLocalDateTime;
        if (currentExpiredDateTime == null) baseExpiredLocalDateTime = LocalDateTime.now();
        else baseExpiredLocalDateTime = LocalDateTime.ofInstant(currentExpiredDateTime, ZoneId.systemDefault());
        LocalDateTime expiredLocalDateTime;
        if (paidType.equals(PaidType.MONTHLY))
            expiredLocalDateTime = baseExpiredLocalDateTime.plusMonths(1);
//            expiredLocalDateTime = baseExpiredLocalDateTime.plusSeconds(20);
        else if (paidType.equals(PaidType.YEARLY))
            expiredLocalDateTime = baseExpiredLocalDateTime.plusYears(1);
//            expiredLocalDateTime = baseExpiredLocalDateTime.plusSeconds(20);
        else return null;
        return expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private Instant calculateExpiredDatetime(Instant currentExpiredDateTime, int numberOfProDays) {
        LocalDateTime baseExpiredLocalDateTime;
        if (currentExpiredDateTime == null) baseExpiredLocalDateTime = LocalDateTime.now();
        else baseExpiredLocalDateTime = LocalDateTime.ofInstant(currentExpiredDateTime, ZoneId.systemDefault());
        LocalDateTime expiredLocalDateTime = baseExpiredLocalDateTime.plusDays(numberOfProDays);
//        LocalDateTime expiredLocalDateTime = baseExpiredLocalDateTime.plusSeconds(30);
        return expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    @Async
    public void callCronjobServerToScheduleDowngradeSubscription(UUID studentId, Instant expiredDateTime) {
        ScheduleDowngradeSubReq requestBody = new ScheduleDowngradeSubReq(
                cronJobSecretKey,
                studentId,
                expiredDateTime
        );
        try {
            ResponseEntity<Boolean> response = cronjobWebClient.post()
                    .uri(MessageConstants.cronjobScheduleDowngradeSubscriptionEndpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .toEntity(Boolean.class)
                    .block();
            if (Objects.equals(response.getBody(), Boolean.FALSE)) {
                log.error("Failed to call cron job scheduler server due to incorrect secret key or invalid expired datetime");
                return;
            }
            log.info("Successfully call cron job scheduler server");
        }
        catch (Exception e) {
            log.error("Error calling cron job scheduler server: " + e);
        }
    }
}
