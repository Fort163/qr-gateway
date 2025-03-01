package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.dto.company.SearchActivityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "COMPANY-SERVICE",
        contextId = "activity",
        path = "/api/v1/activity"
)
public interface CompanyServiceActivityApi {

    @GetMapping({"/{uuid}"})
    ResponseEntity<ActivityDto> byUuid(@PathVariable UUID uuid);

    @GetMapping
    Page<ActivityDto> list(@SpringQueryMap SearchActivityDto searchActivityDto, @SpringQueryMap Pageable pageable);

    @PostMapping
    ResponseEntity<ActivityDto> post(@RequestBody ActivityDto activityDto);

    @PutMapping({"/patch"})
    ResponseEntity<ActivityDto> patch(@RequestBody ActivityDto user);

    @PutMapping
    ResponseEntity<ActivityDto> put(@RequestBody ActivityDto user);

    @DeleteMapping({"/{uuid}"})
    ResponseEntity<Boolean> delete(@PathVariable UUID uuid);

}
