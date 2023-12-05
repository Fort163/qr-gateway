package com.quick.recording.gateway.dto.auth;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SearchRoleDto {

    private String name;
    private Boolean isActive = true;
    private List<UUID> permissionUuid;
    private List<String> permissionName;

}
