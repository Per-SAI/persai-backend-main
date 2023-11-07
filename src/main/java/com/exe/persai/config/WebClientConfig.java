package com.exe.persai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${server.cron-job.host}")
    private String cronjobHost;
    @Value("${gpt.host}")
    private String gptHost;

    @Bean("cronjobWebClient")
    public WebClient webClient() {
        return WebClient.create(cronjobHost);
    }

    @Bean("gptChatWebClient")
    public WebClient gptChatWebClient() {
        return WebClient.create(gptHost);
    }
}
