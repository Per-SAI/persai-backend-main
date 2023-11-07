package com.exe.persai.controller;

import com.exe.persai.controller.api.GptController;
import com.exe.persai.model.request.GptMessageRequest;
import com.exe.persai.model.response.GptMessageResponse;
import com.exe.persai.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GptControllerImpl implements GptController {

    private final GptService gptService;

    @Override
    public ResponseEntity<GptMessageResponse> callChatGpt(GptMessageRequest request) {
        return ResponseEntity.ok(gptService.callChatGpt(request));
    }

    @Override
    public ResponseEntity<List<GptMessageResponse>> getAllGptChat() {
        return ResponseEntity.ok(gptService.getAllGptChat());
    }

//    @GetMapping("/choose-answer")
//    public String callGptToAnswerQuestion(@RequestParam("question") String question, @RequestParam("answers") String[] answers) {
//        String correctAnswer = gptService.callGptToGenerateCorrectAnswer(question, answers);
//        System.out.println(Arrays.asList(answers).contains(correctAnswer));
//        return correctAnswer;
//    }
}
