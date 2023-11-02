package com.quick.recording.gateway.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class LoggerInterceptor implements RequestInterceptor {

    @Value("${gateway.logger:true}")
    private Boolean enabled;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if(enabled) {
            log.info("Resource {} - method {} \n\t\t\turl {}.", requestTemplate.feignTarget().name(), requestTemplate.request().httpMethod(), requestTemplate.request().url());
        }
    }


}
