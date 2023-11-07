package com.exe.persai.controller.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        log.error("AsyncUncaughtException message: " + throwable.getMessage() + "; Method name: " + method.getName());
        for (Object param : params)
            log.error("AsyncUncaughtException: Parameter value: " + param);
    }
}
