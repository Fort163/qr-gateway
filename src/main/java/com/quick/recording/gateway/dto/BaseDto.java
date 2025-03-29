package com.quick.recording.gateway.dto;

import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Put;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
public class BaseDto {

    public BaseDto() {
    }

    public BaseDto(UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull(message = "{validation.uuid}", groups = {Put.class, Patch.class})
    @Id
    private UUID uuid;

}
