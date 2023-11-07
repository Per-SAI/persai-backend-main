package com.exe.persai.service.mapper;

import com.exe.persai.model.entity.Pomodoro;
import com.exe.persai.model.response.PomodoroResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PomodoroMapper {
    PomodoroMapper INSTANCE = Mappers.getMapper(PomodoroMapper.class);

    @Mapping(source = "pomodoro.user.id", target = "userId")
    @Mapping(source = "pomodoro.user.fullName", target = "userFullName")
    PomodoroResponse toPomodoroResponse(Pomodoro pomodoro);
}
