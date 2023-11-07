package com.exe.persai.service.mapper;

import com.exe.persai.constants.MessageConstants;
import com.exe.persai.model.entity.UpgradeRequest;
import com.exe.persai.model.entity.Users;
import com.exe.persai.model.response.BasicUserResponse;
import com.exe.persai.model.response.UpgradeRequestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UpgradeRequestMapper {
    UpgradeRequestMapper INSTANCE = Mappers.getMapper(UpgradeRequestMapper.class);

    @Mapping(source = "upgradeRequest.user", target = "userResponse", qualifiedByName = MessageConstants.GET_BASIC_USER)
    UpgradeRequestResponse toUpgradeRequestResponse(UpgradeRequest upgradeRequest);

    @Named(MessageConstants.GET_BASIC_USER)
    static BasicUserResponse getBasicUser(Users user) {
        return UserMapper.INSTANCE.toBasicUserResponse(user);
    }
}
