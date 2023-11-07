package com.exe.persai.service;

import com.exe.persai.config.GptConfig;
import com.exe.persai.constants.MessageConstants;
import com.exe.persai.model.entity.GptMessage;
import com.exe.persai.model.entity.Users;
import com.exe.persai.model.entity.gpt.ChatGptRequest;
import com.exe.persai.model.entity.gpt.ChatGptResponse;
import com.exe.persai.model.request.GptMessageRequest;
import com.exe.persai.model.response.GptMessageResponse;
import com.exe.persai.repository.GptMessageRepository;
import com.exe.persai.repository.UserRepository;
import com.exe.persai.service.mapper.GptMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.client.OpenAiClient;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GptService {

    private final GptConfig gptConfig;
    @Qualifier("gptChatWebClient")
    private final WebClient gptWebClient;
    private final OpenAiClient aiClient;
    private final UserRepository userRepository;
    private final GptMessageRepository gptMessageRepository;

    public String callGptApi(String question) {
        ChatGptRequest chatGptRequest = new ChatGptRequest(gptConfig, question);
        try {
            ChatGptResponse response = gptWebClient.post()
                    .uri(MessageConstants.gptChatEndpoint)
                    .header("Authorization", "Bearer " + gptConfig.apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(chatGptRequest))
                    .retrieve()
                    .bodyToMono(ChatGptResponse.class)
                    .block();
            if (response != null)
                return response.getChoices().get(0).getMessage().getContent();
            return null;
        }
        catch (Exception e) {
            log.error("Error calling chat GPT API: " + e.getMessage());
            throw new IllegalStateException("Error calling chat GPT API");
        }
    }

    public String generateResponseUsingSpringAI(String question) {
        return aiClient.generate(question);
    }

    public GptMessageResponse callChatGpt(GptMessageRequest request) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        Users user = userRepository.findById(currentUser.getId()).get();
        if (user.getGptRemainingUsage() <= 0)
            throw new AccessDeniedException("You have reached your GPT usage limit");

        if (user.getUserSubscription() != null) aiClient.setModel(user.getUserSubscription().getSubscription().getGptModel());
        String gptAnswer = generateAnswerFromGpt(request.getContent());
        GptMessage gptMessage = GptMessage.builder()
                .user(user)
                .content(request.getContent())
                .gptAnswer(gptAnswer)
                .build();
        gptMessage = gptMessageRepository.save(gptMessage);
        user.setGptRemainingUsage(user.getGptRemainingUsage() - 1);

        //reset aiClient model
        aiClient.setModel("gpt-3.5-turbo");

        return GptMessageMapper.INSTANCE.toGptMessageResponse(gptMessage);
    }

    public List<GptMessageResponse> getAllGptChat() {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        List<GptMessage> gptMessages = gptMessageRepository.findAllByUser_IdOrderByCreatedAtAsc(currentUser.getId());
        return gptMessages.stream().map(GptMessageMapper.INSTANCE::toGptMessageResponse).toList();
    }

    public String callGptToGenerateCorrectAnswer(String question, String[] answers) {
        String prompt = """
                Answer this multiple choice question.
                {question}
                Here is the list of choices:
                {answers}
                Only choose the right choice from the list, or generate the most correct choice, no more explanation.
                """;
        PromptTemplate promptTemplate = new PromptTemplate(prompt);
        promptTemplate.add("question", question);
        promptTemplate.add("answers", Arrays.toString(answers));
        return generateAnswerFromGpt(promptTemplate.render());
    }

    private String generateAnswerFromGpt(String question) {
        try {
            return aiClient.generate(question);
        }
        catch (Exception e) {
            throw new IllegalStateException("Rate limit reached for gpt-3.5-turbo. Limit: 3 / min. Please try again in 20s.");
        }
    }
}
