package com.exe.persai.model.response;

import com.exe.persai.model.enums.Availability;
import com.exe.persai.model.enums.LearningMethod;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PomodoroResponse {
    private Integer id;
    private UUID userId;
    private String userFullName;
    private int studyTime;
    private int shortBreak;
    private int longBreak;
    private int longBreakInterval;
    private LearningMethod learningMethod;
    private Availability status;
}
