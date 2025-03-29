package com.quick.recording.gateway.config.function;

import com.quick.recording.gateway.dto.broker.MessageChangeDataDto;

@FunctionalInterface
public interface RemoteCacheFunction {

    void run(MessageChangeDataDto dto);

}
