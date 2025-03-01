package com.quick.recording.gateway.dto.employee;

import com.quick.recording.gateway.dto.auth.AuthUserDto;
import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeDto {

    private UUID uuid;
    private AuthUserDto user;
    private ProfessionDto name;

}
