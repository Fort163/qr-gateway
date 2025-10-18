package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.ServiceDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "COMPANY-SERVICE",
        contextId = "service",
        path = "/company/api/v1/service")
public interface CompanyServiceServiceApi extends MainRemoteService<ServiceDto> {

    @Override
    default Class<ServiceDto> getType(){
        return ServiceDto.class;
    }

}
