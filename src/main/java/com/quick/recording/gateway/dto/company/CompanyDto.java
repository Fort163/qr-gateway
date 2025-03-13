package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDto extends SmartDto {

    private String name;
    private GeocoderDto geoPosition;
    private List<ScheduleDto> schedules;
    private List<ActivityDto> activity;

}
