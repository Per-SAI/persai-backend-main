package com.exe.persai.controller;

import com.exe.persai.constants.MessageConstants;
import com.exe.persai.controller.api.SubscriptionController;
import com.exe.persai.model.enums.PaidType;
import com.exe.persai.model.enums.UpgradeRequestStatus;
import com.exe.persai.model.request.DowngradeSubscriptionRequest;
import com.exe.persai.model.request.RescheduleDowngradeRequest;
import com.exe.persai.model.request.UpgradeSubscriptionRequest;
import com.exe.persai.model.response.GeneralResponse;
import com.exe.persai.model.response.SubscriptionResponse;
import com.exe.persai.model.response.UpgradeRequestResponse;
import com.exe.persai.model.response.UserResponse;
import com.exe.persai.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SubscriptionControllerImpl implements SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Override
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscription() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @Override
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(String subscriptionId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(subscriptionId.toUpperCase()));
    }

    @Override
    public ResponseEntity<GeneralResponse> requestToUpgradeWithPayment(PaidType paidType) {
        subscriptionService.requestToUpgradeWithPayment(paidType);
//        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(MessageConstants.PAYMENT_LINK)).build();
        return ResponseEntity.status(HttpStatus.OK).body(new GeneralResponse(MessageConstants.PAYMENT_LINK));
    }

    @Override
    public ResponseEntity<List<UpgradeRequestResponse>> getAllUpgradeRequests(UpgradeRequestStatus status, Sort sort) {
        return ResponseEntity.ok(subscriptionService.getAllUpgradeRequests(status, sort));
    }

    @Override
    public ResponseEntity<UpgradeRequestResponse> getUpgradeRequestById(Integer upgradeRequestId) {
        return ResponseEntity.ok(subscriptionService.getUpgradeRequestById(upgradeRequestId));
    }

    @Override
    public ResponseEntity<GeneralResponse> rejectUpgradeRequest(UpgradeSubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.rejectUpgradeRequest(request));
    }

    @Override
    public ResponseEntity<UserResponse> upgradeSubscription(UpgradeSubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.upgradeSubscription(request));
    }

    @Override
    public ResponseEntity<Boolean> downgradeSubscription(DowngradeSubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.downgradeSubscription(request));
    }

    @Override
    public ResponseEntity<String> rescheduleDowngradeSubscription(RescheduleDowngradeRequest request) {
        return ResponseEntity.ok(subscriptionService.rescheduleDowngradeSubscription(request));
    }


}
