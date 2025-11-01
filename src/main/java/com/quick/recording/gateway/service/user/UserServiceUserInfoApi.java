package com.quick.recording.gateway.service.user;

import com.quick.recording.gateway.dto.user.UserInfoDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "activity",
        path = "/user/api/v1/user"
)
public interface UserServiceUserInfoApi extends MainRemoteService<UserInfoDto> {

    @Override
    default Class<UserInfoDto> getType(){
        return UserInfoDto.class;
    }

}
