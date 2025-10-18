package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.config.validation.base.ValidBaseDto;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import com.quick.recording.gateway.dto.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalTime;

@Data
@RedisHash("Service")
public class ServiceDto extends SmartDto {

    @NotNull(
            message = "{validation.name}",
            groups = {Put.class, Post.class}
    )
    private String name;
    @NotNull(
            message = "{validation.service.time.not.null}",
            groups = {Put.class, Post.class}
    )
    @Null(
            message = "{validation.search.null.time.field}",
            groups = {List.class}
    )
    private LocalTime workClock;
    private Byte countPartTime;
    @NotNull(
            message = "{validation.company.not.null}",
            groups = {Put.class, Post.class}
    )
    @ValidBaseDto(
            message = "{validation.base.dto.default}",
            groups = {Post.class, Put.class, Patch.class}
    )
    private BaseDto company;
    @ValidBaseDto(
            message = "{validation.base.dto.default}",
            groups = {Post.class, Put.class, Patch.class}
    )
    private BaseDto employee;

}
