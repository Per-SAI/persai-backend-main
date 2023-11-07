package com.exe.persai.model.response;

import com.exe.persai.model.enums.Visibility;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StudySetResponse {
    private Integer id;
    private String studySetName;
    private String feImageName;
    private CreatorResponse creator;
    private Visibility visibility;
    private boolean status;
    private Instant createdAt;
    private Instant updatedAt;
    private List<QuestionResponse> questionResponses;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class QuestionResponse {
        private Integer id;
        private String question;
        private String[] answers;
        private String correctAnswer;
        private boolean isGptGenerated;
        private String fullGptAnswer;
        private String note;
    }
}
