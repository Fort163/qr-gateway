package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.dto.SmartDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("Activity")
public class ActivityDto extends SmartDto {

    @NotNull(
            message = "{validation.name}"
    )
    private String name;
    @NotNull(
            message = "{validation.description}"
    )
    private String description;

}
