package com.quick.recording.gateway.dto.notification.mail;

import com.quick.recording.gateway.dto.notification.CheckCode;
import com.quick.recording.gateway.dto.notification.CreateCode;
import com.quick.recording.gateway.enumerated.TemplateEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MailCodeDto {

    @Email(message = "{validation.mail.to.email}")
    @NotNull(message = "{validation.mail.to.not.null}")
    private String email;
    @NotNull(message = "{validation.mail.template}", groups = {CreateCode.class})
    private TemplateEnum template;
    @NotNull(message = "{validation.mail.code.not.null}", groups = {CheckCode.class})
    private String code;

}
