package com.quick.recording.gateway.dto.yandex;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GeocoderDto {

    private UUID uuid;
    private Double longitude;
    private Double latitude;
    private String name;
    private List<GeocoderObjectDto> geoObjects;

}
