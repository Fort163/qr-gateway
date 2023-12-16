package com.quick.recording.gateway.dto.yandex;

import lombok.Data;

import java.util.UUID;

@Data
public class GeocoderObjectDto {

    private UUID uuid;
    private String name;
    private String kind;

}
