package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.config.validation.base.ValidBaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@RedisHash("Company")
public class CompanyDto extends SmartDto {

    @NotNull(
            message = "{validation.name}",
            groups = {Put.class, Post.class}
    )
    private String name;
    private String description;
    @NotNull(
            message = "{validation.company.geocoder.not.null}",
            groups = {Put.class}
    )
    @ValidBaseDto(
            message = "{validation.base.dto.default}",
            groups = {Put.class, Patch.class}
    )
    private GeocoderDto geoPosition;
    @NotEmpty(
            message = "{validation.company.schedules.not.empty}",
            groups = {Put.class}
    )
    @ValidBaseDto(
            message = "{validation.list.base.dto.default}",
            groups = {Put.class, Patch.class}
    )
    private List<ScheduleDto> schedules;
    @NotEmpty(
            message = "{validation.company.activities.not.empty}",
            groups = {Put.class}
    )
    @ValidBaseDto(
            message = "{validation.list.base.dto.default}",
            groups = {Post.class, Put.class, Patch.class}
    )
    private List<ActivityDto> activities;
    @ValidBaseDto(
            message = "{validation.list.base.dto.default}",
            groups = {Post.class, Put.class, Patch.class}
    )
    private List<ServiceDto> services;

}
