package com.quick.recording.gateway.service.auth;

import com.quick.recording.gateway.dto.auth.AuthUserDto;
import com.quick.recording.gateway.dto.auth.SearchUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "AUTH-SERVICE",
        contextId = "user",
        path = "/api/user"
)
public interface AuthServiceUserApi {

    @GetMapping("/{uuid}")
    ResponseEntity<AuthUserDto> byUuid(@PathVariable UUID uuid);

    @GetMapping
    Page<AuthUserDto> list(SearchUserDto searchUserDto, Pageable pageable);

    @PutMapping("/patch")
    ResponseEntity<AuthUserDto> patch(@RequestBody AuthUserDto user);

    @PutMapping()
    ResponseEntity<AuthUserDto> put(@RequestBody AuthUserDto user);

    @DeleteMapping("/{uuid}")
    ResponseEntity<Boolean> delete(@PathVariable UUID uuid);

}
