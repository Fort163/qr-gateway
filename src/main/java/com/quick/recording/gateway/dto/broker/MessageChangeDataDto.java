package com.quick.recording.gateway.dto.broker;

import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.enumerated.ChangeDataType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
public class MessageChangeDataDto extends BaseMessage {

    private ChangeDataType type;
    private UUID entityUUID;
    private Class<? extends BaseDto> clazz;

}
