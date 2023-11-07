package com.exe.persai.model.entity;

import com.exe.persai.model.entity.audit.AuditCreatedAt;
import com.exe.persai.model.enums.PaidType;
import com.exe.persai.model.enums.UpgradeRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "upgrade_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpgradeRequest extends AuditCreatedAt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Users user;
    @Column(name = "paid_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaidType paidType;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UpgradeRequestStatus status = UpgradeRequestStatus.PENDING;
}
