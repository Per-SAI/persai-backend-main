package com.exe.persai.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "system_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemConfig implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String field;
    private String value;
    private String note;
}
