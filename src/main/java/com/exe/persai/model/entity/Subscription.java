package com.exe.persai.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription implements Serializable {
    @Id
    private String id;
    @Column(name = "subscription_name", nullable = false)
    private String subscriptionName;
    @Column(nullable = false)
    private int level;
    private String description;
    @Column(name = "price_per_month")
    private BigDecimal pricePerMonth;
    @Column(name = "price_per_year")
    private BigDecimal pricePerYear;
    @Column(name = "supported_features")
    private String supportedFeatures;
    @Column(name = "gpt_model", nullable = false)
    private String gptModel;
    @Column(name = "gpt_limit", nullable = false)
    private int gptLimit;
    @Column(nullable = false)
    @Builder.Default
    private boolean status = true;
}
