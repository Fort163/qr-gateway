package com.quick.recording.gateway.service.auth;

import com.quick.recording.gateway.dto.auth.PermissionDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "AUTH-SERVICE",
        contextId = "permission",
        path = "/api/v1/permission"
)
public interface AuthServicePermissionApi extends MainRemoteService<PermissionDto> {

    @Override
    default Class<PermissionDto> getType(){
        return PermissionDto.class;
    }

}
