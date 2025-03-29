package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "activity",
        path = "/company/api/v1/activity"
)
public interface CompanyServiceActivityApi extends MainRemoteService<ActivityDto> {

    @Override
    default Class<ActivityDto> getType(){
        return ActivityDto.class;
    }

}
