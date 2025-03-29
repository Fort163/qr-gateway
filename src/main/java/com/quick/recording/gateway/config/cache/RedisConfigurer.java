package com.quick.recording.gateway.config.cache;

import com.quick.recording.gateway.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
@RequiredArgsConstructor
public class RedisConfigurer{

    private final RedisProperties redisProperties;

    @Bean
    @ConditionalOnProperty(value = "qr-cache.enabled", havingValue = "true")
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        //TODO(When i understand how the connection in redisinsight works)
        /*redisStandaloneConfiguration.setUsername(redisProperties.getUser());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));*/
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    @ConditionalOnProperty(value = "qr-cache.enabled", havingValue = "true")
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        return template;
    }

}
