package com.quick.recording.gateway.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class SmartEntity extends AuditEntity{

    @Column(name = "is_active")
    private Boolean isActive = true;

    public SmartEntity() {
    }

    public SmartEntity(UUID uuid, String createdBy, LocalDateTime createdWhen,
                       String updatedBy, LocalDateTime updatedWhen, Boolean isActive) {
        super(uuid, createdBy, createdWhen, updatedBy, updatedWhen);
        this.isActive = isActive;
    }
}
