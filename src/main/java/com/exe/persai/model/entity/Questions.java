package com.exe.persai.model.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Questions implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String question;
    @Type(value = StringArrayType.class)
    @Column(name = "answers", columnDefinition = "text[]")
    private String[] answers;
    @Column(name = "correct_answer")
    private String correctAnswer;
    @Column(name = "is_gpt_generated", nullable = false)
    @Builder.Default
    private boolean isGptGenerated = false;
    @Column(name = "full_gpt_answer")
    private String fullGptAnswer;
    @ManyToOne
    @JoinColumn(name = "study_set_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private StudySet studySet;
}
