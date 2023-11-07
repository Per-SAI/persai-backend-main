package com.exe.persai.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreatorResponse {
    private UUID userId;
    private String userFullName;
    private String userEmail;
    private String userAvatar;
}
