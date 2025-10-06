package com.quick.recording.gateway.service.auth;

import com.quick.recording.gateway.dto.auth.AuthUserDto;
import com.quick.recording.gateway.dto.auth.Role2UserDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "AUTH-SERVICE",
        contextId = "user",
        path = "/api/v1/user"
)
public interface AuthServiceUserApi extends MainRemoteService<AuthUserDto> {

    @PutMapping("/role")
    ResponseEntity<Boolean> addRole(@RequestBody @Valid Role2UserDto dto);

    @Override
    default Class<AuthUserDto> getType(){
        return AuthUserDto.class;
    }

}
