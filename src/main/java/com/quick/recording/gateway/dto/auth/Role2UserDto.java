package com.quick.recording.gateway.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class Role2UserDto {

    @NotNull(message = "validation.auth.uuid.user")
    private UUID user;
    @NotNull(message = "validation.auth.uuid.role")
    private UUID role;

}
