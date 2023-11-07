package com.exe.persai.service.mapper;

import com.exe.persai.constants.MessageConstants;
import com.exe.persai.model.entity.Questions;
import com.exe.persai.model.entity.StudySet;
import com.exe.persai.model.entity.Users;
import com.exe.persai.model.response.BasicStudySetResponse;
import com.exe.persai.model.response.CreatorResponse;
import com.exe.persai.model.response.StudySetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface StudySetMapper {
    StudySetMapper INSTANCE = Mappers.getMapper(StudySetMapper.class);

    @Mapping(source = "studySet", target = "feImageName", qualifiedByName = MessageConstants.GET_FE_IMAGE_NAME)
    @Mapping(source = "studySet", target = "creator", qualifiedByName = MessageConstants.GET_CREATOR)
    @Mapping(source = "studySet", target = "questionResponses", qualifiedByName = MessageConstants.GET_QUESTIONS)
    StudySetResponse toStudySetResponse(StudySet studySet);

    @Mapping(source = "studySet", target = "feImageName", qualifiedByName = MessageConstants.GET_FE_IMAGE_NAME)
    @Mapping(source = "studySet", target = "creator", qualifiedByName = MessageConstants.GET_CREATOR)
    BasicStudySetResponse toBasicStudySetResponse(StudySet studySet);

    StudySetResponse.QuestionResponse toQuestionResponse(Questions question);

    @Named(MessageConstants.GET_FE_IMAGE_NAME)
    static String getFeImageName(StudySet studySet) {
        if (studySet.getImage() == null) return null;
        return MessageConstants.host + MessageConstants.getImageEndpoint + studySet.getImage().getFeImageName();
    }

    @Named(MessageConstants.GET_QUESTIONS)
    static List<StudySetResponse.QuestionResponse> getQuestions(StudySet studySet) {
        return studySet.getQuestionsList().stream().map(INSTANCE::toQuestionResponse).toList();
    }

    @Named(MessageConstants.GET_CREATOR)
    static CreatorResponse getCreator(StudySet studySet) {
        Users user = studySet.getUser();
        String userAvatar;
        if (user.getAvatar() == null) userAvatar = null;
        else if (user.getAvatar().getGgImageLink() != null) userAvatar = user.getAvatar().getFeImageName();
        else userAvatar = MessageConstants.host + MessageConstants.getImageEndpoint + user.getAvatar().getFeImageName();
        CreatorResponse creator = new CreatorResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                userAvatar
        );
        return creator;
    }
}
