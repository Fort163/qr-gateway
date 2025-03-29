package com.quick.recording.gateway.main.service.remote;

import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.main.controller.MainController;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.lang.Nullable;

import java.util.Locale;

public interface MainRemoteService<Dto extends BaseDto> extends MainController<Dto> {

    Class<Dto> getType();

    @Nullable
    default String cacheName(){
        boolean supportCache = getType().isAnnotationPresent(RedisHash.class);
        if(supportCache){
            return getType().getAnnotation(RedisHash.class).value().toLowerCase(Locale.ROOT);
        }
        return null;
    }


}
