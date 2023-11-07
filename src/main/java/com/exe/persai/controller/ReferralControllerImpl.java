package com.exe.persai.controller;

import com.exe.persai.controller.api.ReferralController;
import com.exe.persai.model.request.ReferralCodeRequest;
import com.exe.persai.model.response.UserResponse;
import com.exe.persai.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReferralControllerImpl implements ReferralController {

    private final SubscriptionService subscriptionService;

    @Override
    public ResponseEntity<UserResponse> enterReferralCode(ReferralCodeRequest request) {
        return ResponseEntity.ok(subscriptionService.enterReferralCode(request));
    }
}
