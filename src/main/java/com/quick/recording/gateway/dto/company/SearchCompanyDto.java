package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import lombok.Data;

import java.util.List;

@Data
public class SearchCompanyDto {

    private String name;
    private GeocoderDto geoPosition;
    private Long longitude;
    private Long latitude;
    private List<String> activityUuid;

}
