package com.quick.recording.gateway.dto;

import com.quick.recording.gateway.dto.util.Get;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Put;
import com.quick.recording.gateway.dto.util.Search;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
public class BaseDto {

    public BaseDto() {
    }

    public BaseDto(UUID uuid) {
        this.uuid = uuid;
    }

    @Null(
            message = "{validation.search.uuid}",
            groups = {Get.class, Search.class}
    )
    @NotNull(message = "{validation.uuid}", groups = {Put.class, Patch.class})
    @Id
    private UUID uuid;

}
