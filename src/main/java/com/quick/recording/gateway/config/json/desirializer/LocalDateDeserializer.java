package com.quick.recording.gateway.config.json.desirializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@JsonComponent
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        if(Objects.nonNull(jsonParser.getText())) {
            return LocalDate.parse(jsonParser.getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }
        return null;
    }
}
