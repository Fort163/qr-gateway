package com.quick.recording.gateway.main.service;

import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.Delete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.UUID;

public interface MainService <Entity extends SmartEntity, Dto extends BaseDto>  {

    Dto byUuid(UUID uuid);

    Page<Dto> search(Dto dto, Pageable pageable);

    Dto post(Dto dto);

    Dto patch(Dto dto);

    Dto put(Dto dto);

    Boolean delete(UUID uuid, Delete delete);

    Entity save(Entity entity);

}
