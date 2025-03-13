package com.quick.recording.gateway.dto.auth;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PermissionDto extends SmartDto {

    @NotNull(message = "{validation.auth.permission}", groups = {Put.class, Post.class})
    private String permission;

}
