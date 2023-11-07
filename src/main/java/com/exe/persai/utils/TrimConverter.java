package com.exe.persai.utils;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.stereotype.Component;

@Component
public class TrimConverter extends StdConverter<String, String> {

    @Override
    public String convert(String s) {
        return s.trim();
    }
}
