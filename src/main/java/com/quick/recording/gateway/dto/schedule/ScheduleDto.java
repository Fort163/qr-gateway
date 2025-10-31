package com.quick.recording.gateway.dto.schedule;

import com.quick.recording.gateway.config.validation.base.ValidBaseDto;
import com.quick.recording.gateway.config.validation.schedule.ValidSchedule;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.List;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import com.quick.recording.gateway.enumerated.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalTime;

@Data
@RedisHash("Schedule")
@ValidSchedule(
        message = "{validation.schedule.type}",
        groups = {Post.class, Patch.class, Put.class}
)
public class ScheduleDto extends SmartDto {

    @NotNull(
            message = "{validation.schedule.clock.from}",
            groups = {Put.class, Post.class}
    )
    @Null(
            message = "{validation.search.null.time.field}",
            groups = {List.class}
    )
    private LocalTime clockFrom;
    @NotNull(
            message = "{validation.schedule.clock.to}",
            groups = {Put.class, Post.class}
    )
    @Null(
            message = "{validation.search.null.time.field}",
            groups = {List.class}
    )
    private LocalTime clockTo;
    @NotNull(
            message = "{validation.schedule.work}",
            groups = {Put.class, Post.class}
    )
    private Boolean work;
    @NotNull(
            message = "{validation.schedule.dayOfWeek}",
            groups = {Put.class, Post.class}
    )
    private DayOfWeek dayOfWeek;
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
