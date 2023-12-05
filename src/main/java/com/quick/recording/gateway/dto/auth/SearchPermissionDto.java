package com.quick.recording.gateway.dto.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class SearchPermissionDto {

    private String permission;
    private Boolean isActive = true;
    private UUID roleUuid;
    private String roleName;

}
