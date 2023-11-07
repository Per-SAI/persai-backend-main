package com.exe.persai.model.entity;

import com.exe.persai.model.entity.audit.AuditCreatedAndUpdatedAt;
import com.exe.persai.model.enums.Visibility;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "study_set")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudySet extends AuditCreatedAndUpdatedAt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "study_set_name", nullable = false)
    private String studySetName;
    @ManyToOne
    @JoinColumn(name = "image_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Image image;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Users user;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Visibility visibility = Visibility.PUBLIC;
    @Column(nullable = false)
    @Builder.Default
    private boolean status = true;
    @OneToMany(mappedBy = "studySet", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Questions> questionsList;
}
