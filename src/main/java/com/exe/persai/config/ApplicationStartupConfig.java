package com.exe.persai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ApplicationStartupConfig implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private Environment environment;
    public static String host;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        host = environment.getProperty("server.host");
    }
}
