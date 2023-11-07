package com.exe.persai.service.mapper;

import com.exe.persai.constants.MessageConstants;
import com.exe.persai.model.entity.Users;
import com.exe.persai.model.enums.SubscriptionType;
import com.exe.persai.model.response.BasicUserResponse;
import com.exe.persai.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "user", target = "feImageName", qualifiedByName = MessageConstants.GET_FE_IMAGE_NAME)
    @Mapping(source = "user", target = "referralCode", qualifiedByName = MessageConstants.GET_REFERRAL_CODE)
    @Mapping(source = "user", target = "subscription", qualifiedByName = MessageConstants.GET_SUBSCRIPTION)
    UserResponse toUserResponse(Users user);

    @Mapping(source = "user", target = "feImageName", qualifiedByName = MessageConstants.GET_FE_IMAGE_NAME)
    @Mapping(source = "user", target = "subscription", qualifiedByName = MessageConstants.GET_SUBSCRIPTION)
    BasicUserResponse toBasicUserResponse(Users user);


    @Named(MessageConstants.GET_FE_IMAGE_NAME)
    static String getFeImageName(Users user) {
        if (user.getAvatar() == null) return null;
        if (user.getAvatar().getGgImageLink() != null) return user.getAvatar().getFeImageName();
        return MessageConstants.host + MessageConstants.getImageEndpoint + user.getAvatar().getFeImageName();
    }

    @Named(MessageConstants.GET_REFERRAL_CODE)
    static UserResponse.ReferralCodeResponse getReferralCode(Users user) {
        return user.getReferralCode() == null ? null : new UserResponse.ReferralCodeResponse(
                user.getReferralCode().getCode(),
                user.getReferralCode().getReferenceNumber(),
                user.getReferralCode().isUsingReferralCode()
        );
    }

    @Named(MessageConstants.GET_SUBSCRIPTION)
    static UserResponse.UserSubscriptionResponse getSubscription(Users user) {
        if (user.getUserSubscription() == null) return null;
        String currentSubscriptionId;
        String paidSubscriptionId;
        if (user.getUserSubscription().getSubscription().getId().equals(SubscriptionType.BASIC.toString())
                || user.getUserSubscription().getExpiredDatetime().isAfter(Instant.now()))
            currentSubscriptionId = paidSubscriptionId = user.getUserSubscription().getSubscription().getId();
        else {
            currentSubscriptionId = SubscriptionType.BASIC.toString();
            paidSubscriptionId = user.getUserSubscription().getSubscription().getId();
        }
        return new UserResponse.UserSubscriptionResponse(
                currentSubscriptionId,
                paidSubscriptionId,
                user.getUserSubscription().getPaidType(),
                user.getUserSubscription().getExpiredDatetime()
        );
    }
}
