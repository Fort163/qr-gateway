package com.quick.recording.gateway.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuditDto extends BaseDto {

    private String createdBy;
    private LocalDateTime createdWhen;
    private String updatedBy;
    private LocalDateTime updatedWhen;

    public AuditDto() {
    }

    public AuditDto(UUID uuid, String createdBy, LocalDateTime createdWhen, String updatedBy, LocalDateTime updatedWhen) {
        super(uuid);
        this.createdBy = createdBy;
        this.createdWhen = createdWhen;
        this.updatedBy = updatedBy;
        this.updatedWhen = updatedWhen;
    }

}
