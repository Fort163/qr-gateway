package com.quick.recording.gateway.config.filter;

import com.quick.recording.gateway.Constant;
import com.quick.recording.gateway.config.context.QRContext;
import com.quick.recording.gateway.config.context.QRContextHandler;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QRContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        QRContext context = QRContextHandler.getContext();
        context.setTrackId(httpServletRequest.getHeader(Constant.TRACK_ID_HEADER_NAME));
        filterChain.doFilter(httpServletRequest,servletResponse);
    }

}
