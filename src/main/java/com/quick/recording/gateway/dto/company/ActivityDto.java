package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("Activity")
public class ActivityDto extends SmartDto {

    @NotNull(
            message = "{validation.name}",
            groups = {Put.class, Post.class}
    )
    private String name;
    @NotNull(
            message = "{validation.description}",
            groups = {Put.class, Post.class}
    )
    private String description;

}
