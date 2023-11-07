package com.exe.persai.service.mapper;

import com.exe.persai.model.entity.UserQuestionNote;
import com.exe.persai.model.response.UserQuestionNoteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserQuestionNoteMapper {
    UserQuestionNoteMapper INSTANCE = Mappers.getMapper(UserQuestionNoteMapper.class);

    @Mapping(source = "note.user.id", target = "userId")
    @Mapping(source = "note.question.id", target = "questionId")
    UserQuestionNoteResponse toUserQuestionNoteResponse(UserQuestionNote note);
}
