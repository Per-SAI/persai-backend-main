package com.exe.persai.model.request;

import com.exe.persai.constants.ValidationConstants;
import com.exe.persai.model.enums.Visibility;
import com.exe.persai.utils.TrimConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class CreateStudySetRequest {
    @NotBlank
    @Size(min = 2, message = ValidationConstants.NAME_LENGTH_MESSAGE)
    @Size(max = 50, message = ValidationConstants.NAME_LENGTH_MESSAGE)
    @JsonDeserialize(converter = TrimConverter.class)
    private String studySetName;
    @Schema(description = "BASIC only support PUBLIC, PRO can create PUBLIC or PRIVATE set")
    @NotNull
    private Visibility visibility;
    @NotEmpty
    private List<CreateQuestionRequest> questionsList;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CreateQuestionRequest {
        @NotBlank
        @Size(min = 1, message = ValidationConstants.NAME_LENGTH_MESSAGE)
        private String question;
        @NotEmpty
        private String[] answers;
        @Schema(description = "Can be blank when you want GPT to generate answer")
        private String correctAnswer;
        @Schema(description = "If yes, GPT will generate correct answer for this question")
        private boolean isGptGenerated;
    }
}
