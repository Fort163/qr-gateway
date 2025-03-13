package com.quick.recording.gateway.entity;

import com.quick.recording.gateway.listener.AuditListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
@EntityListeners(AuditListener.class)
public class AuditEntity extends BaseEntity {

    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_when")
    private LocalDateTime createdWhen;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "updated_when")
    private LocalDateTime updatedWhen;

    public AuditEntity() {
    }

    public AuditEntity(UUID uuid, String createdBy, LocalDateTime createdWhen,
                       String updatedBy, LocalDateTime updatedWhen) {
        super(uuid);
        this.createdBy = createdBy;
        this.createdWhen = createdWhen;
        this.updatedBy = updatedBy;
        this.updatedWhen = updatedWhen;
    }
}
