package com.quick.recording.gateway.config.context;

import java.util.Objects;

public class QRContextHandler {

    private static final ThreadLocal<QRContext> QR_CONTEXT_THREAD = new ThreadLocal<>();

    public static QRContext getContext(){
        QRContext context = QR_CONTEXT_THREAD.get();
        if(Objects.isNull(context)){
            context = createContext();
            QR_CONTEXT_THREAD.set(context);
        }
        return context;
    }

    private static QRContext createContext() {
        return new QRContext();
    }

}
