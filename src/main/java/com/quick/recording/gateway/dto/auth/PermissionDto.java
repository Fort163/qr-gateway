package com.quick.recording.gateway.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PermissionDto {

    @NotNull(message = "UUID is required",groups = {PutPermission.class})
    private UUID uuid;
    @NotNull(message = "Permission is required",groups = {PutPermission.class,PostPermission.class})
    private String permission;

}
