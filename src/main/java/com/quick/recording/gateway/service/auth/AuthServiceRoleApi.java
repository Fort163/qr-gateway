package com.quick.recording.gateway.service.auth;

import com.quick.recording.gateway.dto.auth.RoleDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "AUTH-SERVICE",
        contextId = "role",
        path = "/api/v1/role"
)
public interface AuthServiceRoleApi extends MainRemoteService<RoleDto> {

    @Override
    default Class<RoleDto> getType(){
        return RoleDto.class;
    }

}
