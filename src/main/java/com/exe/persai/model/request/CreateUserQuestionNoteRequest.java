package com.exe.persai.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserQuestionNoteRequest {
    @NotBlank
    private String note;
}
