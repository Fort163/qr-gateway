package com.quick.recording.gateway.config;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public record MessageUtil(MessageSource messageSource) {

    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public String create(String key, @Nullable Object... param) {
        return messageSource.getMessage(key, param, getLocale());
    }

}
