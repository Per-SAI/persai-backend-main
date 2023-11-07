package com.exe.persai.service;

import com.exe.persai.constants.MessageConstants;
import com.exe.persai.model.entity.*;
import com.exe.persai.model.enums.PaidType;
import com.exe.persai.model.enums.SubscriptionType;
import com.exe.persai.model.enums.UserStatus;
import com.exe.persai.repository.PomodoroRepository;
import com.exe.persai.repository.SubscriptionRepository;
import com.exe.persai.repository.UserRepository;
import com.exe.persai.repository.UserSubscriptionRepository;
import com.exe.persai.utils.EmailPlatform;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RegisterService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PomodoroRepository pomodoroRepository;
    private final EmailService emailService;
    private final ImageService imageService;
    private final ReferralCodeService referralCodeService;

    public Users registerBasicUserViaGoogle(GoogleIdToken.Payload payload) {
        String email  = payload.getEmail();
        String fullName = String.valueOf(payload.get("name"));
        String googlePicture = String.valueOf(payload.get("picture"));
        Image avatar = imageService.createImageFromGooglePicture(googlePicture);
        Users user = Users.builder()
                .email(email)
                .fullName(fullName)
                .avatar(avatar)
                .status(UserStatus.SUCCEED)
                .enabled(true)
                .build();
        user = userRepository.save(user);
        ReferralCode referralCode = referralCodeService.createReferralCodeForNewUser(user);
        user.setReferralCode(referralCode);
        UserSubscription userSubscription = registerBasicSubscriptionForNewUser(user);
        user.setUserSubscription(userSubscription);
        user.setGptRemainingUsage(userSubscription.getSubscription().getGptLimit());
        Pomodoro pomodoro = Pomodoro.builder()
                .user(user)
                .build();
        pomodoro = pomodoroRepository.save(pomodoro);
        //send email
        emailService.send(MessageConstants.FROM_EMAIL, email, MessageConstants.REGISTER_EMAIL_SUBJECT,
                EmailPlatform.buildWelcomeEmail(user));

        return user;
    }

    private UserSubscription registerBasicSubscriptionForNewUser(Users user) {
        Subscription basicSubscription = subscriptionRepository.findById(SubscriptionType.BASIC.toString()).get();
        UserSubscription userSubscription = UserSubscription.builder()
                .user(user)
                .subscription(basicSubscription)
                .paidType(PaidType.NO)
                .build();
        return userSubscriptionRepository.save(userSubscription);
    }
}
