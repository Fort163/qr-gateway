package com.quick.recording.gateway.dto.notification.mail;

import com.quick.recording.gateway.dto.SmartDto;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("NotificationMail")
public class NotificationMailDto extends SmartDto {

    private UUID messageID;
    private String subject;
    private String fromEmail;
    private String recipients;
    private String content;
    private Boolean send;

    @Builder
    public NotificationMailDto(UUID uuid, String createdBy, LocalDateTime createdWhen, String updatedBy,
                               LocalDateTime updatedWhen, Boolean isActive, UUID messageID, String subject,
                               String fromEmail, String recipients, String content, Boolean send) {
        super(uuid, createdBy, createdWhen, updatedBy, updatedWhen, isActive);
        this.messageID = messageID;
        this.subject = subject;
        this.fromEmail = fromEmail;
        this.recipients = recipients;
        this.content = content;
        this.send = send;
    }

}

