package com.exe.persai.controller.api;

import com.exe.persai.constants.SwaggerApiTag;
import com.exe.persai.model.request.ReferralCodeRequest;
import com.exe.persai.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/referral")
public interface ReferralController {

    @Tag(name = SwaggerApiTag.REFERRAL)
    @Operation(
            summary = "Enter other user's referral code to get free dates using PRO Subscription"
    )
    @PutMapping
    @PreAuthorize("hasAuthority('STUDENT')")
    ResponseEntity<UserResponse> enterReferralCode(@RequestBody @Valid ReferralCodeRequest request);
}
