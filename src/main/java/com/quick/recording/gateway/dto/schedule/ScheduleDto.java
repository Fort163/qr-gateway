package com.quick.recording.gateway.dto.schedule;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.enumerated.DayOfWeek;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("Schedule")
public class ScheduleDto extends SmartDto {

    private String clockFrom;
    private String clockTo;
    private Boolean work;
    private DayOfWeek dayOfWeek;

}
