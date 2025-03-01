package com.quick.recording.gateway.service.auth;

import com.quick.recording.gateway.dto.auth.RoleDto;
import com.quick.recording.gateway.dto.auth.SearchRoleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "AUTH-SERVICE",
        contextId = "role",
        path = "/api/v1/role"
)
public interface AuthServiceRoleApi {

    @GetMapping({"/{uuid}"})
    ResponseEntity<RoleDto> byUuid(@PathVariable UUID uuid);

    @GetMapping
    Page<RoleDto> list(@SpringQueryMap SearchRoleDto searchRoleDto, @SpringQueryMap Pageable pageable);

    @PostMapping
    ResponseEntity<RoleDto> post(@RequestBody RoleDto role);

    @PutMapping({"/patch"})
    ResponseEntity<RoleDto> patch(@RequestBody RoleDto role);

    @PutMapping
    ResponseEntity<RoleDto> put(@RequestBody RoleDto role);

    @DeleteMapping({"/{uuid}"})
    ResponseEntity<Boolean> delete(@PathVariable UUID uuid);

}
