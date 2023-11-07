package com.exe.persai.model.enums;

import lombok.Getter;

public enum SubscriptionType {
    BASIC(0), PRO(1);
    @Getter
    private final int level;

    SubscriptionType(int level) {
        this.level = level;
    }
}
