package com.exe.persai.model.response;

import com.exe.persai.model.entity.Users;
import com.exe.persai.service.mapper.UserMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserResponse userResponse;

    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userResponse = null;
    }

    public LoginResponse(String accessToken, String refreshToken, Users user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userResponse = UserMapper.INSTANCE.toUserResponse(user);
    }
}
