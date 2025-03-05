package com.quick.recording.gateway.config.error.exeption;

import com.quick.recording.gateway.config.MessageUtil;

public class NotFoundException extends RuntimeException {

    public NotFoundException(MessageUtil messageUtil, Class<?> clazz, Object object) {
        super(messageUtil.create("exception.not.found", clazz.getSimpleName(), object));
    }

    public NotFoundException(String message) {
        super(message);
    }
}
