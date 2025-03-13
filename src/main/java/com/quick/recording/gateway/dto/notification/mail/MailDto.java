package com.quick.recording.gateway.dto.notification.mail;

import com.quick.recording.gateway.enumerated.TemplateEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MailDto {

    @Email(message = "{validation.mail.to.email}")
    @NotNull(message = "{validation.mail.to.not.null}")
    private String to;
    private List<@Email(message = "{validation.mail.cc.emails}") String> cc;
    private List<@Email(message = "{validation.mail.bcc.emails}") String> bcc;
    @NotNull(message = "{validation.mail.template}")
    private TemplateEnum template;
    @NotNull(message = "{validation.mail.subject.not.null}")
    private String subject;
    private String text;

}
