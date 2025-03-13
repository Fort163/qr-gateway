package com.quick.recording.gateway.dto.yandex;

import com.quick.recording.gateway.dto.SmartDto;
import lombok.Data;

import java.util.List;

@Data
public class GeocoderDto extends SmartDto {

    private Double longitude;
    private Double latitude;
    private String name;
    private List<GeocoderObjectDto> geoObjects;

}
