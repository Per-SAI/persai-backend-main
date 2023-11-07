package com.exe.persai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GptConfig {
    @Value("${gpt.model}")
    public String gptModel;
    @Value("${gpt.max-tokens}")
    public int maxTokens;
    @Value("${gpt.api-key}")
    public String apiKey;
}
