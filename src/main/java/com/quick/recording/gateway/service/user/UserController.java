package com.quick.recording.gateway.service.user;

import com.quick.recording.gateway.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "USER-SERVICE",contextId = "user", path = "/user")
public interface UserController {

    @GetMapping("/currentUser")
    ResponseEntity<UserDto> getCurrentUser();

    @GetMapping("/userByCompany/{uuid}")
    ResponseEntity<List<UserDto>> usersByCompany(@PathVariable String uuid);

    @GetMapping("/userByName/{name}")
    ResponseEntity<UserDto> getUserByName(@PathVariable String name);

}
