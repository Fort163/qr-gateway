package com.quick.recording.gateway.dto.employee;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.auth.AuthUserDto;
import lombok.Data;

@Data
public class EmployeeDto extends SmartDto {

    private AuthUserDto user;
    private ProfessionDto name;

}
