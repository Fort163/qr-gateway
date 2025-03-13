package com.quick.recording.gateway.dto.yandex;

import com.quick.recording.gateway.dto.SmartDto;
import lombok.Data;

@Data
public class GeocoderObjectDto extends SmartDto {

    private String name;
    private String kind;

}
