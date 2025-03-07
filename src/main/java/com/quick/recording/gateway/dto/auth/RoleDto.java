package com.quick.recording.gateway.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RoleDto {

    @NotNull(message = "validation.uuid", groups = {PutRole.class})
    private UUID uuid;
    @NotNull(message = "validation.name", groups = {PutRole.class, PostRole.class})
    private String name;
    @NotEmpty(message = "validation.auth.role.not.empty", groups = {PutRole.class, PostRole.class})
    private List<PermissionDto> permissions;
    private Boolean isActive;

}
