package com.exe.persai.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
public class GptMessageResponse {
    private UUID id;
    private UUID userId;
    private String content;
    private String gptAnswer;
    private Instant createdAt;

}
