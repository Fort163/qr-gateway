package com.quick.recording.gateway.util;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.main.service.remote.CacheableMainRemoteServiceAbstract;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import com.quick.recording.gateway.main.service.remote.MainRemoteServiceAbstract;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class ContextUtil {

    @Nullable
    @SuppressWarnings({"unchecked","rawtypes"})
    public static MainRemoteService<? extends SmartDto> findService(ApplicationContext context,
                                                                    Class<? extends SmartDto> dtoClass){
        Optional<CacheableMainRemoteServiceAbstract> cacheable =
                context.getBeansOfType(CacheableMainRemoteServiceAbstract.class).values().stream()
                        .filter(item -> item.getType().equals(dtoClass))
                        .findFirst();
        if(cacheable.isPresent()){
            return cacheable.get();
        }
        Optional<MainRemoteServiceAbstract> main =
                context.getBeansOfType(MainRemoteServiceAbstract.class).values().stream()
                        .filter(item -> item.getType().equals(dtoClass))
                        .findFirst();
        if(main.isPresent()){
            return main.get();
        }
        Optional<MainRemoteService> remote =
                context.getBeansOfType(MainRemoteService.class).values().stream()
                        .filter(item -> item.getType().equals(dtoClass))
                        .findFirst();
        return remote.orElse(null);
    }

}
