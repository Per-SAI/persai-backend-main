package com.exe.persai.model.response;

import com.exe.persai.model.enums.PaidType;
import com.exe.persai.model.enums.UpgradeRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class UpgradeRequestResponse {
    private Integer id;
    private BasicUserResponse userResponse;
    private PaidType paidType;
    private BigDecimal price;
    private Instant createdAt;
    private UpgradeRequestStatus status;
}
