package com.exe.persai.model.entity;

import com.exe.persai.model.entity.audit.AuditCreatedAt;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "gpt_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GptMessage extends AuditCreatedAt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Users user;
    private String content;
    @Column(name = "gpt_answer")
    private String gptAnswer;
}
