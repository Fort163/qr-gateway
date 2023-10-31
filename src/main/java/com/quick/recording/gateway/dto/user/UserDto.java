package com.quick.recording.gateway.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private UUID uuid;
    private String name;
    private Integer age;
    private String work;

}
