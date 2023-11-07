package com.exe.persai.model.entity;

import com.exe.persai.model.entity.audit.AuditCreatedAt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "image")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image extends AuditCreatedAt implements Serializable {
    @Id
    @Column(name = "fe_image_name")
    private String feImageName;
    @Column(name = "s3_image_name")
    private String s3ImageName;
    @Column(name = "gg_image_link")
    private String ggImageLink;
    @Column(nullable = false)
    @Builder.Default
    private boolean status = true;
}
