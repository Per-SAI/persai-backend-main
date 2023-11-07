package com.exe.persai.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class DowngradeSubscriptionRequest {
    @NotBlank
    private String secretKey;
    @NotNull
    private UUID userId;
}
