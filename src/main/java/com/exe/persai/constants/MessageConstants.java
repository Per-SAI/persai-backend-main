package com.exe.persai.constants;

import com.exe.persai.config.ApplicationStartupConfig;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

@UtilityClass
public class MessageConstants {
    //email
    public final String FROM_EMAIL = "persai.work@gmail.com";

    public final String REGISTER_EMAIL_SUBJECT = "Welcome to PerSAI, the best way to study!";
    public final String REFERRAL_EMAIL_SUBJECT = "Thanks for spreading the word about PerSAI!";

    //host and endpoints
    public final String host = ApplicationStartupConfig.host;
    public final String getImageEndpoint = "/api/v1/image/";
    public final String gptChatEndpoint = "/v1/chat/completions";
    public final String cronjobScheduleDowngradeSubscriptionEndpoint = "/api/v1/subscription/schedule-downgrade";
    public final String PAYMENT_LINK = "https://me.momo.vn/PerSai";

    //mapper name method
    public final String GET_FE_IMAGE_NAME = "getFeImageName";
    public final String GET_REFERRAL_CODE = "getReferralCode";
    public final String GET_SUBSCRIPTION = "getSubscription";
    public final String GET_QUESTIONS = "getQuestions";
    public final String GET_CREATOR = "getCreator";
    public final String GET_BASIC_USER = "getBasicUser";
    public final String GET_NOTE = "getNote";

    //other
    public final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public final String EXCEL_SHEET_NAME = "Questions";
}
