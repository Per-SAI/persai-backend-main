package com.exe.persai.service.mapper;

import com.exe.persai.model.entity.GptMessage;
import com.exe.persai.model.response.GptMessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GptMessageMapper {
    GptMessageMapper INSTANCE = Mappers.getMapper(GptMessageMapper.class);

    @Mapping(target = "userId", source = "gptMessage.user.id")
    GptMessageResponse toGptMessageResponse(GptMessage gptMessage);
}
