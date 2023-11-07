package com.exe.persai.model.request;

import com.exe.persai.constants.ValidationConstants;
import com.exe.persai.model.enums.Theme;
import com.exe.persai.utils.TrimConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Arrays;

@Getter
@Setter
public class UpdateUserRequest {
    @Pattern(regexp = ValidationConstants.NAME_REGEX, message = ValidationConstants.NAME_FORMAT_MESSAGE)
    @Size(min = 8, message = ValidationConstants.NAME_LENGTH_MESSAGE)
    @Size(max = 50, message = ValidationConstants.NAME_LENGTH_MESSAGE)
    @JsonDeserialize(converter = TrimConverter.class)
    private String fullName;
    @Schema(example = "DEFAULT", description = "DEFAULT, GRAY, RED, PINK, GRAPE, VIOLET, INDIGO, BLUE, CYAN, TEAL, GREEN, LIME, YELLOW, ORANGE")
    private Theme theme;
}
