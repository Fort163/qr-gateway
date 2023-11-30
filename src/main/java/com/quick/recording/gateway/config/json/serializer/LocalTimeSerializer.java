package com.quick.recording.gateway.config.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


@JsonComponent
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {
    @Override
    public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(Objects.nonNull(localTime)) {
            jsonGenerator.writeString(localTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }
}
