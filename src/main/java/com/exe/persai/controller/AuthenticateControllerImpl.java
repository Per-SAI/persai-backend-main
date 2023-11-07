package com.exe.persai.controller;

import com.exe.persai.controller.api.AuthenticateController;
import com.exe.persai.model.response.LoginResponse;
import com.exe.persai.service.AuthenticateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticateControllerImpl implements AuthenticateController {
    private final AuthenticateService authenticateService;

    @Override
    public ResponseEntity<LoginResponse> getNewAccessTokenFromRefreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(authenticateService.getNewAccessTokenFromRefreshToken(request));
    }
}
