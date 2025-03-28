package com.quick.recording.gateway.service.notification;

import com.quick.recording.gateway.dto.notification.mail.NotificationMailDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "notificationMail",
        path = "/notification/api/v1/notification/mail"
)
public interface NotificationServiceNotificationMailApi extends MainRemoteService<NotificationMailDto> {

    @Override
    default Class<NotificationMailDto> getType(){
        return NotificationMailDto.class;
    }

}
