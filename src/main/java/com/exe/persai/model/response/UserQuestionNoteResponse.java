package com.exe.persai.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserQuestionNoteResponse {
    private UUID userId;
    private Integer questionId;
    private String note;
}
