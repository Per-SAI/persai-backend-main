package com.exe.persai.model.response;

import com.exe.persai.model.enums.PaidType;
import com.exe.persai.model.enums.Role;
import com.exe.persai.model.enums.Theme;
import com.exe.persai.model.enums.UserStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserResponse {
    private UUID id;
    private String email;
    private String fullName;
    private String feImageName;
    private Role role;
    private BigDecimal earnedMoney;
    private int gptRemainingUsage;
    private Theme theme;
    private UserStatus status;
    private boolean enabled;
    private ReferralCodeResponse referralCode;
    private UserSubscriptionResponse subscription;
    private Instant createdAt;
    private Instant updatedAt;


    @AllArgsConstructor
    @Getter
    @Setter
    public static class ReferralCodeResponse {
        private String referralCode;
        private int referenceNumber;
        private boolean usingReferralCode;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserSubscriptionResponse {
        private String currentSubscriptionId;
        private String paidSubscriptionId;
        private PaidType paidType;
        private Instant expiredDatetime;
    }
}
