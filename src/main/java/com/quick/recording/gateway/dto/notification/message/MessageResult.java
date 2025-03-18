package com.quick.recording.gateway.dto.notification.message;

import com.quick.recording.gateway.enumerated.Result;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@Builder
public class MessageResult {

    private Result result;
    @Nullable
    private UUID messageId;
    @Nullable
    private String resultText;

}
