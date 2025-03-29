package com.quick.recording.gateway.dto.broker;

import com.quick.recording.gateway.config.context.QRContextHandler;
import lombok.Data;

@Data
public abstract class BaseMessage {

    private String trackId = QRContextHandler.getContext().getTrackId();

}
