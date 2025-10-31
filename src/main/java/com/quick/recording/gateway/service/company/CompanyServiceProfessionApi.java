package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.ProfessionDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "profession",
        path = "/company/api/v1/profession")
public interface CompanyServiceProfessionApi extends MainRemoteService<ProfessionDto> {

    @Override
    default Class<ProfessionDto> getType(){
        return ProfessionDto.class;
    }

}
