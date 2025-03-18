package com.quick.recording.gateway.interceptor;

import com.quick.recording.gateway.Constant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class LocaleInterceptor implements RequestInterceptor {

    @Value("${gateway.locale.enabled:true}")
    private Boolean enabled;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (enabled) {
            requestTemplate.header(HttpHeaders.COOKIE,
                    String.format("%s=%s", Constant.LOCALE_COOKIE_NAME, LocaleContextHolder.getLocale()
                    )
            );
        }
    }


}
