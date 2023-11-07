package com.exe.persai.controller.api;

import com.exe.persai.constants.SwaggerApiTag;
import com.exe.persai.model.enums.PaidType;
import com.exe.persai.model.enums.UpgradeRequestStatus;
import com.exe.persai.model.request.DowngradeSubscriptionRequest;
import com.exe.persai.model.request.RescheduleDowngradeRequest;
import com.exe.persai.model.request.UpgradeSubscriptionRequest;
import com.exe.persai.model.response.GeneralResponse;
import com.exe.persai.model.response.SubscriptionResponse;
import com.exe.persai.model.response.UpgradeRequestResponse;
import com.exe.persai.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/subscription")
@Validated
public interface SubscriptionController {

    @Tag(name = SwaggerApiTag.SUBSCRIPTION)
    @Operation(
            summary = "Get all subscriptions"
    )
    @GetMapping
    ResponseEntity<List<SubscriptionResponse>> getAllSubscription();

    @Tag(name = SwaggerApiTag.SUBSCRIPTION)
    @Operation(
            summary = "Get subscription by ID"
    )
    @GetMapping("/{subscription_id}")
    ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable("subscription_id") String subscriptionId);

    @Tag(name = SwaggerApiTag.SUBSCRIPTION)
    @Operation(
            summary = "Student requests to upgrade subscription when choosing to fulfill the payment"
    )
    @GetMapping("/upgrade-request/current")
    @PreAuthorize("hasAuthority('STUDENT')")
    ResponseEntity<GeneralResponse> requestToUpgradeWithPayment(@RequestParam("paidType") PaidType paidType);

    @Tag(name = SwaggerApiTag.SUBSCRIPTION)
    @Operation(
            summary = "Get all upgrade requests from students (ONLY ADMIN)"
    )
    @GetMapping("/upgrade-request/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<List<UpgradeRequestResponse>> getAllUpgradeRequests(@RequestParam(value = "status", required = false) UpgradeRequestStatus status,
                                                                       @ParameterObject Sort sort);

    @Tag(name = SwaggerApiTag.SUBSCRIPTION)
    @Operation(
            summary = "Get upgrade request by ID (ONLY ADMIN)"
    )
    @GetMapping("/upgrade-request/{upgrade_request_id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<UpgradeRequestResponse> getUpgradeRequestById(@PathVariable("upgrade_request_id") Integer upgradeRequestId);

    @Tag(name = SwaggerApiTag.SUBSCRIPTION)
    @Operation(
            summary = "Reject request to upgrade subscription for a specific user (ONLY ADMIN)"
    )
    @PutMapping("/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<GeneralResponse> rejectUpgradeRequest(@RequestBody @Valid UpgradeSubscriptionRequest request);

    @Tag(name = SwaggerApiTag.SUBSCRIPTION)
    @Operation(
            summary = "Make request to upgrade subscription for a specific user (ONLY ADMIN)"
    )
    @PutMapping("/upgrade")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<UserResponse> upgradeSubscription(@RequestBody @Valid UpgradeSubscriptionRequest request);

    @Tag(name = SwaggerApiTag.SUBSCRIPTION)
    @Operation(
            summary = "Cron Job server automatically calls this API to downgrade subscription after expiredDateTime"
    )
    @PutMapping("/downgrade")
    ResponseEntity<Boolean> downgradeSubscription(@RequestBody @Valid DowngradeSubscriptionRequest request);

    @Tag(name = SwaggerApiTag.SUBSCRIPTION)
    @Operation(
            summary = "Reschedule downgrade subscription for PRO users"
    )
    @PutMapping("/downgrade/reschedule")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<String> rescheduleDowngradeSubscription(@RequestBody @Valid RescheduleDowngradeRequest request);
}
