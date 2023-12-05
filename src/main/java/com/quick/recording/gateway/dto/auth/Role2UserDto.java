package com.quick.recording.gateway.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class Role2UserDto {

    @NotNull(message = "UUID user is required")
    private UUID user;
    @NotNull(message = "UUID role is required")
    private UUID role;

}
