package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CompanyDto {

    private UUID uuid;
    private String name;
    private GeocoderDto geoPosition;
    private List<ScheduleDto> schedules;
    private List<ActivityDto> activity;

}
