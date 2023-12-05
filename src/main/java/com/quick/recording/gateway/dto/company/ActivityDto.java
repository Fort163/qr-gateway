package com.quick.recording.gateway.dto.company;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ActivityDto {

    private UUID uuid;
    @NotNull(
            message = "Name is required"
    )
    private String name;
    @NotNull(
            message = "Description is required"
    )
    private String description;
    private Boolean isActive = true;

}
