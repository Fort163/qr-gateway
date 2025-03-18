package com.quick.recording.gateway.service.notification;

import com.quick.recording.gateway.dto.notification.CheckCode;
import com.quick.recording.gateway.dto.notification.CreateCode;
import com.quick.recording.gateway.dto.notification.mail.MailCodeDto;
import com.quick.recording.gateway.dto.notification.mail.MailDto;
import com.quick.recording.gateway.dto.notification.mail.MailResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "mailSender",
        path = "/notification/api/v1/mail/sender"
)
public interface NotificationServiceMailSenderApi {

    @PostMapping
    ResponseEntity<MailResult> send(
            @Validated MailDto mail,
            @RequestPart(value = "files", required = false) MultipartFile[] files);

    @PostMapping(path = "/code")
    ResponseEntity<MailResult> code(@RequestBody @Validated({CreateCode.class}) MailCodeDto code);

    @PostMapping(path = "/code/check")
    ResponseEntity<Boolean> check(@RequestBody @Validated({CheckCode.class}) MailCodeDto mailCodeDto);

}
