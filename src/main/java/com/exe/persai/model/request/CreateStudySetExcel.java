package com.exe.persai.model.request;

import com.exe.persai.constants.ValidationConstants;
import com.exe.persai.model.enums.Visibility;
import com.exe.persai.utils.TrimConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudySetExcel {
    @NotBlank
    @Size(min = 2, message = ValidationConstants.NAME_LENGTH_MESSAGE)
    @Size(max = 50, message = ValidationConstants.NAME_LENGTH_MESSAGE)
    @JsonDeserialize(converter = TrimConverter.class)
    private String studySetName;
    @Schema(description = "BASIC only support PUBLIC, PRO can create PUBLIC or PRIVATE set")
    @NotNull
    private Visibility visibility;
}
