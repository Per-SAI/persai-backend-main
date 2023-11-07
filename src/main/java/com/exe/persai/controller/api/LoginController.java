package com.exe.persai.controller.api;

import com.exe.persai.constants.SwaggerApiTag;
import com.exe.persai.model.request.LoginRequest;
import com.exe.persai.model.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/login")
public interface LoginController {

    @Tag(name = SwaggerApiTag.LOGIN)
    @Operation(
            summary = "API for logging in via Google"
    )
    @PostMapping("/google")
    ResponseEntity<LoginResponse> loginUserViaGoogle(@RequestBody @Valid LoginRequest request);
}
