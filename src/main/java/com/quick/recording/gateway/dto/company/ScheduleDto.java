package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.enumerated.DayOfWeek;
import lombok.Data;

import java.util.UUID;

@Data
public class ScheduleDto {

    private UUID uuid;
    private String clockFrom;
    private String clockTo;
    private Boolean work;
    private DayOfWeek dayOfWeek;

}
