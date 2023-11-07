package com.exe.persai.model.entity;

import com.exe.persai.model.enums.PaidType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSubscription implements Serializable {
    @Id
    private UUID userId;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @MapsId
    private Users user;
    @ManyToOne
    @JoinColumn(name = "subscription_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Subscription subscription;
    @Column(name = "paid_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaidType paidType;
    @Column(name = "expired_datetime")
    private Instant expiredDatetime;
}
