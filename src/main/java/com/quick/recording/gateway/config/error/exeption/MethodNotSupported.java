package com.quick.recording.gateway.config.error.exeption;

import com.quick.recording.gateway.config.MessageUtil;

public class MethodNotSupported extends RuntimeException {

    public MethodNotSupported(MessageUtil messageUtil) {
        super(messageUtil.create("exception.method.not.supported"));
    }

}
