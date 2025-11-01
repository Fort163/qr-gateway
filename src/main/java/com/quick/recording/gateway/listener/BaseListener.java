package com.quick.recording.gateway.listener;

import com.quick.recording.gateway.entity.BaseEntity;
import jakarta.persistence.PrePersist;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class BaseListener {

    @PrePersist
    private void prePersist(BaseEntity entity) {
        if (Objects.isNull(entity.getUuid())) {
            entity.setUuid(UUID.randomUUID());
        }
    }

}
