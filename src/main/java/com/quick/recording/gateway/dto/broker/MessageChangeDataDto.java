package com.quick.recording.gateway.dto.broker;

import com.quick.recording.gateway.enumerated.ChangeDataType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MessageChangeDataDto extends BaseMessage {

    private ChangeDataType type;
    private UUID entityUUID;

}
