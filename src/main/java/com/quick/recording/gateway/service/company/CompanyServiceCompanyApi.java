package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "company",
        path = "/company/api/v1/company")
public interface CompanyServiceCompanyApi extends MainRemoteService<CompanyDto> {

    @Override
    default Class<CompanyDto> getType(){
        return CompanyDto.class;
    }

}
