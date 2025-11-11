package com.quick.recording.gateway.listener;

import com.quick.recording.gateway.entity.AuditEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class AuditListener {

    @Value("${spring.application.name}")
    private String appName;

    @PrePersist
    private void prePersist(AuditEntity entity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = appName;
        if (Objects.nonNull(authentication)) {
            userName = authentication.getName();
        }
        entity.setCreatedWhen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        entity.setUpdatedWhen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        entity.setCreatedBy(userName);
        entity.setUpdatedBy(userName);
    }

    @PreUpdate
    private void preUpdate(AuditEntity entity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = appName;
        if (Objects.nonNull(authentication)) {
            userName = authentication.getName();
        }
        entity.setUpdatedWhen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        entity.setUpdatedBy(userName);
    }

}
