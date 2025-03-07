package com.quick.recording.gateway.dto.employee;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ProfessionDto {

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
