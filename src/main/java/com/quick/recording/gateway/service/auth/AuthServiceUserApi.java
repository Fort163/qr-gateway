package com.quick.recording.gateway.service.auth;

import com.quick.recording.gateway.dto.auth.AuthUserDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "AUTH-SERVICE",
        contextId = "user",
        path = "/api/v1/user"
)
public interface AuthServiceUserApi extends MainRemoteService<AuthUserDto> {

    @Override
    default Class<AuthUserDto> getType(){
        return AuthUserDto.class;
    }

}
