package com.exe.persai.model.response;

import com.exe.persai.model.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BasicStudySetResponse {
    private Integer id;
    private String studySetName;
    private String feImageName;
    private CreatorResponse creator;
    private Visibility visibility;
    private Instant createdAt;
    private Instant updatedAt;
}
