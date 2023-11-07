package com.exe.persai.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RescheduleDowngradeRequest {
    @NotBlank
    private String secretKey;
}
