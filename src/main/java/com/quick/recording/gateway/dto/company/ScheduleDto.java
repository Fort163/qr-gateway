package com.quick.recording.gateway.dto.company;

import lombok.Data;

import java.util.UUID;

@Data
public class ScheduleDto {

    private UUID uuid;
    private String fromTime;
    private String toTime;

}
