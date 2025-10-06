package com.quick.recording.gateway.main.service.local;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.Delete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface MainService<Entity extends SmartEntity, Dto extends SmartDto> {

    Dto byUuid(UUID uuid);

    Page<Dto> search(Dto dto, Pageable pageable);

    Dto post(Dto dto);

    Dto patch(Dto dto);

    Dto put(Dto dto);

    Boolean delete(UUID uuid, Delete delete);

    Boolean restore(UUID uuid);

    Entity save(Entity entity);

    Entity byUuidEntity(UUID uuid);

    List<Entity> saveAll(Collection<Entity> list);

    List<Entity> findAll();

    Class<Dto> getType();

    @Nullable
    default String cacheName() {
        boolean supportCache = getType().isAnnotationPresent(RedisHash.class);
        if (supportCache) {
            return getType().getAnnotation(RedisHash.class).value().toLowerCase(Locale.ROOT);
        }
        return null;
    }

}
