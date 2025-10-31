package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.EmployeeDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "employee",
        path = "/company/api/v1/employee")
public interface CompanyServiceEmployeeApi extends MainRemoteService<EmployeeDto> {

    @Override
    default Class<EmployeeDto> getType(){
        return EmployeeDto.class;
    }

}
