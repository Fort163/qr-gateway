package com.quick.recording.gateway.mapper;


import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface MainMapper<Entity, Dto> {

    Entity toEntity(Dto dto);

    Dto toDto(Entity entity);

    List<Dto> toDtoList(List<Entity> list);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Entity toEntityWithoutNull(Dto dto, @MappingTarget Entity entity);


    Entity toEntity(Dto dto, @MappingTarget Entity entity);

}
