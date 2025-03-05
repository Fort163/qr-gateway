package com.quick.recording.gateway.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class LoggerInterceptor implements RequestInterceptor {

    @Value("${gateway.logger:true}")
    private Boolean enabled;

    private final MessageUtil messageUtil;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (enabled) {
            log.info(messageUtil.create(
                    "logger.resource.method.url",
                    requestTemplate.feignTarget().name(),
                    requestTemplate.request().httpMethod(),
                    requestTemplate.request().url())
            );
        }
    }


}
