package com.quick.recording.gateway.dto;

import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Put;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
public class BaseDto {

    public BaseDto() {
    }

    public BaseDto(UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull(message = "{validation.uuid}", groups = {Put.class, Patch.class})
    private UUID uuid;

}
