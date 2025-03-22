package com.quick.recording.gateway.dto.notification.message;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.enumerated.MessageType;
import com.quick.recording.gateway.enumerated.SendType;
import com.quick.recording.gateway.enumerated.Project;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessageDto extends SmartDto {

    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    private String fromUser;
    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    private String toUser;
    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    private SendType sendType;
    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    private MessageType messageType;
    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    private Project project;
    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    private String messagePath;
    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    private Boolean send;
    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    @NotNull(message = "{validation.message}", groups = {Post.class})
    private String message;
    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    private String messageCode;
    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    private String jsonObject;
    @Null(message = "{validation.field.cannot.be.changed.notification.message}", groups = {Patch.class})
    private String path;
    private Boolean received;

}
