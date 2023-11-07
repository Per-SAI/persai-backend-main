package com.exe.persai.controller.api;

import com.exe.persai.constants.SwaggerApiTag;
import com.exe.persai.model.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/new-access-token")
public interface AuthenticateController {

    @Tag(name = SwaggerApiTag.AUTHENTICATE)
    @Operation(
            summary = "Get new Access token by Refresh token",
            description = "Include refresh token as bearer token in request header to get new access token together with new refresh token"
    )
    @GetMapping
    ResponseEntity<LoginResponse> getNewAccessTokenFromRefreshToken(HttpServletRequest request);
}
