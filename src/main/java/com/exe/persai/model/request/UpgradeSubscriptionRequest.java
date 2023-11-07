package com.exe.persai.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpgradeSubscriptionRequest {
    @Schema(description = "Upgrade request ID of student upgrade request")
    @NotNull
    private Integer upgradeRequestId;
}
