package com.quick.recording.gateway.service.notification;

import com.quick.recording.gateway.dto.notification.mail.NotificationMailDto;
import com.quick.recording.gateway.main.controller.MainController;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "activity",
        path = "/notification/api/v1/notification/mail"
)
public interface NotificationServiceNotificationMailApi extends MainController<NotificationMailDto> {



}
