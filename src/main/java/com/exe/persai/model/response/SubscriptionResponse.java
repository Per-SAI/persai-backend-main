package com.exe.persai.model.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SubscriptionResponse {
    private String id;
    private String subscriptionName;
    private String description;
    private BigDecimal pricePerMonth;
    private BigDecimal pricePerYear;
    private String supportedFeatures;
    private String gptModel;
    private int gptLimit;
}
