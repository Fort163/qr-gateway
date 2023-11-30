package com.quick.recording.gateway.config.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {

    private String service;
    private String message;
    private String debugMessage;
    private List<String> errors;
    private ApiError parentError;

}
