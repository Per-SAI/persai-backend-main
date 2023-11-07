package com.exe.persai.controller;

import com.exe.persai.controller.api.LoginController;
import com.exe.persai.model.request.LoginRequest;
import com.exe.persai.model.response.LoginResponse;
import com.exe.persai.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginControllerImpl implements LoginController {
    private final LoginService loginService;
    @Override
    public ResponseEntity<LoginResponse> loginUserViaGoogle(LoginRequest request) {
        return ResponseEntity.ok(loginService.authenticateGoogle(request.getIdToken()));
    }
}
