package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.dto.user.UserDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CompanyDto {

    private UUID uuid;
    private String name;
    private List<ScheduleDto> schedules;
    private List<UserDto> users;

}
