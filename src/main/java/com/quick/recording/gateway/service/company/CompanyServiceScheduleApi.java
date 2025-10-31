package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.ServiceDto;
import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "schedule",
        path = "/company/api/v1/schedule")
public interface CompanyServiceScheduleApi extends MainRemoteService<ScheduleDto> {

    @Override
    default Class<ScheduleDto> getType(){
        return ScheduleDto.class;
    }

}
