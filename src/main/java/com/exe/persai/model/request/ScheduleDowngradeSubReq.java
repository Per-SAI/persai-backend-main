package com.exe.persai.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleDowngradeSubReq {
    @NotBlank
    private String secretKey;
    @NotNull
    private UUID userId;
    @NotNull
    private Instant time;
}
