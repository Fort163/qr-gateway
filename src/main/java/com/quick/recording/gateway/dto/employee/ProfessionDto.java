package com.quick.recording.gateway.dto.employee;

import com.quick.recording.gateway.dto.SmartDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfessionDto extends SmartDto {

    @NotNull(
            message = "{validation.name}"
    )
    private String name;
    @NotNull(
            message = "{validation.description}"
    )
    private String description;

}
