package com.exe.persai.controller.api;

import com.exe.persai.constants.SwaggerApiTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/welcome")
public interface WelcomeController {

    @Tag(name = SwaggerApiTag.WELCOME)
    @Operation(
            summary = "API for testing welcome users from server"
    )
    @GetMapping
    String welcomeUser(@RequestParam("username") String username);
}
