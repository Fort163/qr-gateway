package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.ScheduleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "COMPANY-SERVICE",contextId = "company", path = "/schedule")
public interface ScheduleController {

    @GetMapping("/schedule/{uuid}")
    ResponseEntity<ScheduleDto> scheduleByCompanyUuid(@PathVariable String uuid);

}
