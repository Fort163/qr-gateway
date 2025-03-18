package com.quick.recording.gateway.interceptor;

import com.quick.recording.gateway.Constant;
import com.quick.recording.gateway.config.context.QRContextHandler;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class TrackingInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(Constant.TRACK_ID_HEADER_NAME, QRContextHandler.getContext().getTrackId());
    }

}
