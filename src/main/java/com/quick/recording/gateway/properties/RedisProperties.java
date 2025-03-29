package com.quick.recording.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("qr-cache.redis.server")
@Data
public class RedisProperties {

    private String host;
    private int port;
    private String user;
    private String password;

}
