package com.quick.recording.gateway.config.function;

import com.quick.recording.gateway.dto.broker.MessageChangeDataDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CacheConsumer implements Consumer<MessageChangeDataDto> {

    private List<RemoteCacheFunction> functions = new ArrayList<>();

    @Override
    public void accept(MessageChangeDataDto dto) {
        if (Objects.nonNull(functions) && !functions.isEmpty()) {
            functions.forEach(function -> function.run(dto));
        }
    }

    public void addFunction(RemoteCacheFunction function) {
        this.functions.add(function);
    }
}
