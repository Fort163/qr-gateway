package com.quick.recording.gateway.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmartDto extends AuditDto{

    private Boolean isActive = true;

    public SmartDto() {
    }

    public SmartDto(UUID uuid, String createdBy, LocalDateTime createdWhen, String updatedBy,
                    LocalDateTime updatedWhen, Boolean isActive) {
        super(uuid, createdBy, createdWhen, updatedBy, updatedWhen);
        this.isActive = isActive;
    }

}
