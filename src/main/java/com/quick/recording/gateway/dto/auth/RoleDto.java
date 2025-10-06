package com.quick.recording.gateway.dto.auth;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;
import java.util.UUID;

@Data
@RedisHash("Role")
public class RoleDto extends SmartDto {

    @NotNull(message = "{validation.name}", groups = {Put.class, Post.class})
    private String name;
    @NotEmpty(message = "{validation.auth.role.not.empty}", groups = {Put.class, Post.class})
    private List<PermissionDto> permissions;

}
