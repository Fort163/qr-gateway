package com.quick.recording.gateway.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RoleDto {

    @NotNull(message = "UUID is required",groups = {PutRole.class})
    private UUID uuid;
    @NotNull(message = "Name is required",groups = {PutRole.class,PostRole.class})
    private String name;
    @NotEmpty(message = "The role must support at least one permission",groups = {PutRole.class,PostRole.class})
    private List<PermissionDto> permissions;
    private Boolean isActive;

}
