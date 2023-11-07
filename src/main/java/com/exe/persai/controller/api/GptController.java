package com.exe.persai.controller.api;

import com.exe.persai.constants.SwaggerApiTag;
import com.exe.persai.model.request.GptMessageRequest;
import com.exe.persai.model.response.GptMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/gpt")
public interface GptController {

    @Tag(name = SwaggerApiTag.CHAT_GPT)
    @Operation(
            summary = "Submit question to ask GPT for answer"
    )
    @PostMapping
    ResponseEntity<GptMessageResponse> callChatGpt(@RequestBody @Valid GptMessageRequest request);

    @Tag(name = SwaggerApiTag.CHAT_GPT)
    @Operation(
            summary = "Get all GPT chat"
    )
    @GetMapping
    ResponseEntity<List<GptMessageResponse>> getAllGptChat();
}
