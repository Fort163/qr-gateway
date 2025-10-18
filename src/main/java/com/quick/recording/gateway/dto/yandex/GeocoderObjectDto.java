package com.quick.recording.gateway.dto.yandex;

import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import lombok.Data;

@Data
public class GeocoderObjectDto extends BaseDto {

    private String name;
    private String kind;
    private BaseDto geocoder;

}
