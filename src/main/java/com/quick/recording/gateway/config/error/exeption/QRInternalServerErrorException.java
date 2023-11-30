package com.quick.recording.gateway.config.error.exeption;

import jakarta.ws.rs.InternalServerErrorException;

public class QRInternalServerErrorException extends InternalServerErrorException {

    public QRInternalServerErrorException(String serverName, String error) {
        super(String.format("Service with name %s catch error %s", serverName, error));
    }

    private QRInternalServerErrorException(String message) {
        super(message);
    }

}
