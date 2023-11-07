package com.exe.persai.model.request;

import com.exe.persai.constants.ValidationConstants;
import com.exe.persai.model.enums.Availability;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePomodoroRequest {
    @Schema(example = "1500", description = "min: 300(s)")
    @Min(value = 300, message = ValidationConstants.STUDY_TIME_MIN_MESSAGE)
    private int studyTime;
    @Schema(example = "300", description = "min: 60(s)")
    @Min(value = 60, message = ValidationConstants.SHORT_BREAK_MIN_MESSAGE)
    private int shortBreak;
    @Schema(example = "900", description = "min: 300(s)")
    @Min(value = 300, message = ValidationConstants.LONG_BREAK_MIN_MESSAGE)
    private int longBreak;
    @Schema(example = "4", description = "min: 2")
    @Min(value = 2, message = ValidationConstants.LONG_BREAK_INTERVAL_MIN_MESSAGE)
    private int longBreakInterval;
    @Schema(description = "ON or OFF")
    @NotNull
    private Availability status;
}
