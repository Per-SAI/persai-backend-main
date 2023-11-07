package com.exe.persai.model.response;

import com.exe.persai.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class BasicUserResponse {
    private UUID id;
    private String email;
    private String fullName;
    private String feImageName;
    private UserStatus status;
    private UserResponse.UserSubscriptionResponse subscription;
}
