package com.quick.recording.gateway.dto.yandex;

import com.quick.recording.gateway.config.validation.base.ValidBaseDto;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GeocoderDto extends SmartDto {

    @NotNull(
            message = "{validation.required}",
            groups = {Put.class, Post.class}
    )
    private Double longitude;
    @NotNull(
            message = "{validation.required}",
            groups = {Put.class, Post.class}
    )
    private Double latitude;
    @NotNull(
            message = "{validation.name}",
            groups = {Put.class, Post.class}
    )
    private String name;
    @NotNull(
            message = "{validation.base.dto.default}",
            groups = {Put.class, Post.class}

    )
    @ValidBaseDto(
            message = "{validation.base.dto.default}",
            groups = {Post.class, Put.class, Patch.class}
    )
    private BaseDto company;
    @ValidBaseDto(
            message = "{validation.list.base.dto.default}",
            groups = {Put.class, Patch.class}

    )
    @NotEmpty(
            message = "{validation.list.default}",
            groups = {Post.class, Put.class}
    )
    private List<GeocoderObjectDto> geoObjects;

}
