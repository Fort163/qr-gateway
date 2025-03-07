package com.quick.recording.gateway.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PermissionDto {

    @NotNull(message = "validation.uuid", groups = {PutPermission.class})
    private UUID uuid;
    @NotNull(message = "validation.auth.permission", groups = {PutPermission.class, PostPermission.class})
    private String permission;

}
