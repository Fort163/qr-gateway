package com.quick.recording.gateway.entity;

import com.quick.recording.gateway.listener.BaseListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.util.UUID;

@MappedSuperclass
@Data
@EntityListeners(BaseListener.class)
public class BaseEntity {

    @Id
    @Column(name = "uuid")
    protected UUID uuid;

    public BaseEntity() {
    }

    public BaseEntity(UUID uuid) {
        this.uuid = uuid;
    }
}
