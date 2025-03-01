package com.quick.recording.gateway.service.auth;

import com.quick.recording.gateway.dto.auth.PermissionDto;
import com.quick.recording.gateway.dto.auth.SearchPermissionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "AUTH-SERVICE",
        contextId = "permission",
        path = "/api/v1/permission"
)
public interface AuthServicePermissionApi {

    @GetMapping({"/{uuid}"})
    ResponseEntity<PermissionDto> byUuid(@PathVariable UUID uuid);

    @GetMapping
    Page<PermissionDto> list(@SpringQueryMap SearchPermissionDto searchPermissionDto, @SpringQueryMap Pageable pageable);

    @PostMapping
    ResponseEntity<PermissionDto> post(@RequestBody PermissionDto role);

    @PutMapping({"/patch"})
    ResponseEntity<PermissionDto> patch(@RequestBody PermissionDto role);

    @PutMapping
    ResponseEntity<PermissionDto> put(@RequestBody PermissionDto role);

    @DeleteMapping({"/{uuid}"})
    ResponseEntity<Boolean> delete(@PathVariable UUID uuid);

}
