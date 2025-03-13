package com.quick.recording.gateway.dto.schedule;

import com.quick.recording.gateway.dto.AuditDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.enumerated.DayOfWeek;
import lombok.Data;

import java.util.UUID;

@Data
public class ScheduleDto extends SmartDto {

    private String clockFrom;
    private String clockTo;
    private Boolean work;
    private DayOfWeek dayOfWeek;

}
