package com.quick.recording.gateway.test;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public record ResultRequest<Result>(Result result, HttpStatusCode code, ResponseEntity<String> originalResponse) {

    public Result getResult() {
        return result;
    }

    public HttpStatusCode getCode() {
        return code;
    }

    public ResponseEntity<String> getOriginalResponse() {return originalResponse;}

}
