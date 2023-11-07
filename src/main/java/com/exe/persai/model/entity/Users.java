package com.exe.persai.model.entity;

import com.exe.persai.model.entity.audit.AuditCreatedAndUpdatedAt;
import com.exe.persai.model.enums.Role;
import com.exe.persai.model.enums.Theme;
import com.exe.persai.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users extends AuditCreatedAndUpdatedAt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String email;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    private String password;
    @ManyToOne
    @JoinColumn(name = "avatar_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Image avatar;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.STUDENT;
    @Column(name = "earned_money", nullable = false)
    @Builder.Default
    private BigDecimal earnedMoney = BigDecimal.ZERO;
    @Column(name = "gpt_remaining_usage", nullable = false)
    @Builder.Default
    private int gptRemainingUsage = 0;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Theme theme = Theme.DEFAULT;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ReferralCode referralCode;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserSubscription userSubscription;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Pomodoro> pomodoro;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<StudySet> studySets;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<GptMessage> gptMessages;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<UserQuestionNote> notes;

}
