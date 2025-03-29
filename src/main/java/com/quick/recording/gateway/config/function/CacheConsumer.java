package com.quick.recording.gateway.config.function;

import com.quick.recording.gateway.dto.broker.MessageChangeDataDto;
import lombok.Setter;

import java.util.Objects;
import java.util.function.Consumer;

@Setter
public class CacheConsumer implements Consumer<MessageChangeDataDto> {

    private RemoteCacheFunction function;

    @Override
    public void accept(MessageChangeDataDto dto) {
        if(Objects.nonNull(function)){
            function.run(dto);
        }
    }

}
