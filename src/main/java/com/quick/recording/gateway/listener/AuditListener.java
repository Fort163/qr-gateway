package com.quick.recording.gateway.listener;

import com.quick.recording.gateway.entity.AuditEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditListener {

    @PrePersist
    private void prePersist(AuditEntity entity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        entity.setCreatedWhen(LocalDateTime.now());
        entity.setUpdatedWhen(LocalDateTime.now());
        entity.setCreatedBy(authentication.getName());
        entity.setUpdatedBy(authentication.getName());
    }

    @PreUpdate
    private void preUpdate(AuditEntity entity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        entity.setUpdatedWhen(LocalDateTime.now());
        entity.setUpdatedBy(authentication.getName());
    }

}
