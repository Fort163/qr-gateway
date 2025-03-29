package com.quick.recording.gateway.service.notification;

import com.quick.recording.gateway.dto.notification.message.NotificationMessageDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "notificationMessage",
        path = "/notification/api/v1/notification/message"
)
public interface NotificationServiceNotificationMessageApi extends MainRemoteService<NotificationMessageDto> {

    @Override
    default Class<NotificationMessageDto> getType(){
        return NotificationMessageDto.class;
    }

}
