package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "COMPANY-SERVICE", contextId = "company", path = "/api/v1/schedule")
public interface ScheduleController {

    @GetMapping("/schedule/{uuid}")
    ResponseEntity<ScheduleDto> scheduleByCompanyUuid(@PathVariable String uuid);

}
