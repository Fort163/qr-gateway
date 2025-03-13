package com.quick.recording.gateway.config.error.exeption;

import com.quick.recording.gateway.config.MessageUtil;
import jakarta.ws.rs.InternalServerErrorException;

public class QRInternalServerErrorException extends InternalServerErrorException {

    public QRInternalServerErrorException(MessageUtil messageUtil, String serverName, String error) {
        super(messageUtil.create("exception.internal.server.error", serverName, error));
    }

    public QRInternalServerErrorException(String message) {
        super(message);
    }

}
