package com.quick.recording.gateway.mapper;


import org.mapstruct.*;

import java.util.List;

public interface MainMapper<Entity, Dto> {

    @Named(value = "toEntityDefault")
    Entity toEntity(Dto dto);

    @Named(value = "toDtoDefault")
    Dto toDto(Entity entity);

    List<Dto> toDtoList(List<Entity> list);

    @IterableMapping(qualifiedByName = "toEntityDefault")
    List<Entity> toEntityList(List<Dto> list);

    @Named(value = "toEntityWithoutNull")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Entity toEntityWithoutNull(Dto dto, @MappingTarget Entity entity);

    @Named(value = "toEntityWithNull")
    Entity toEntity(Dto dto, @MappingTarget Entity entity);

}
