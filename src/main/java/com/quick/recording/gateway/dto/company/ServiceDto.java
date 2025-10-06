package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.dto.SmartDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalTime;

@Data
@RedisHash("Service")
public class ServiceDto extends SmartDto {

    @NotNull(
            message = "{validation.name}"
    )
    private String name;
    @NotNull(
            message = "{validation.name}"
    )
    private LocalTime time;
    private Byte countPartTime;
    private CompanyDto company;
    private EmployeeDto employee;

}
