package com.exe.persai.model.entity;

import com.exe.persai.model.enums.Availability;
import com.exe.persai.model.enums.LearningMethod;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pomodoro")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pomodoro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Users user;
    @Column(name = "study_time", nullable = false)
    @Builder.Default
    private int studyTime = 1500;
    @Column(name = "short_break", nullable = false)
    @Builder.Default
    private int shortBreak = 300;
    @Column(name = "long_break", nullable = false)
    @Builder.Default
    private int longBreak = 900;
    @Column(name = "long_break_interval", nullable = false)
    @Builder.Default
    private int longBreakInterval = 4;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LearningMethod learningMethod = LearningMethod.ALL;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Availability status = Availability.ON;
}
