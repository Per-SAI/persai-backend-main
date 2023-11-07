package com.exe.persai.service.mapper;

import com.exe.persai.model.entity.Subscription;
import com.exe.persai.model.response.SubscriptionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionMapper INSTANCE = Mappers.getMapper(SubscriptionMapper.class);

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);
}
