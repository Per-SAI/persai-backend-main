package com.exe.persai.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "referral_code")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReferralCode implements Serializable {
    @Id
    private UUID userId;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @MapsId
    private Users user;
    @Column(nullable = false)
    private String code;
    @Column(name = "reference_number", nullable = false)
    @Builder.Default
    private int referenceNumber = 0;
    @Column(name = "using_referral_code", nullable = false)
    @Builder.Default
    private boolean usingReferralCode = false;
}
