package com.quick.recording.gateway.dto.company;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ActivityDto {

    private UUID uuid;
    @NotNull(
            message = "validation.name"
    )
    private String name;
    @NotNull(
            message = "validation.description"
    )
    private String description;
    private Boolean isActive = true;

}
