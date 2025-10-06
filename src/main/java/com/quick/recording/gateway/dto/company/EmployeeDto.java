package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.enumerated.CompanyHierarchyEnum;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;
import java.util.UUID;

@Data
@RedisHash("Employee")
public class EmployeeDto extends SmartDto {

    private UUID authId;
    private CompanyDto company;
    private ProfessionDto name;
    private List<CompanyHierarchyEnum> permissions;
    private List<ServiceDto> services;

}
