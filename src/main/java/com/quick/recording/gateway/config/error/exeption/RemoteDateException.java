package com.quick.recording.gateway.config.error.exeption;

import com.quick.recording.gateway.config.MessageUtil;

import java.io.IOException;

public class RemoteDateException extends IOException {

    public RemoteDateException(MessageUtil messageUtil, Class<?> service) {
        super(messageUtil.create("exception.remote.data", service.getSimpleName()));
    }

    public RemoteDateException(String message) {
        super(message);
    }

}
