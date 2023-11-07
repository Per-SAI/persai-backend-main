package com.exe.persai.model.entity.gpt;

import com.exe.persai.config.GptConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ChatGptRequest {
    private String model;
    private List<Message> messages;
    private int max_tokens;

    public ChatGptRequest(GptConfig gptConfig, String content) {
        this.model = gptConfig.gptModel;
        this.messages = Collections.singletonList(new Message("user", content));
        this.max_tokens = gptConfig.maxTokens;
    }

}
