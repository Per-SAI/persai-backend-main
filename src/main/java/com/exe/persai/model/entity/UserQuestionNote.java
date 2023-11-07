package com.exe.persai.model.entity;

import com.exe.persai.model.entity.compositeKey.UserQuestionNoteKey;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user_question_note")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserQuestionNote implements Serializable {
    @EmbeddedId
    private UserQuestionNoteKey id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Users user;
    @ManyToOne
    @JoinColumn(name = "question_id")
    @MapsId("questionId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Questions question;
    @Column(nullable = false)
    private String note;
}
