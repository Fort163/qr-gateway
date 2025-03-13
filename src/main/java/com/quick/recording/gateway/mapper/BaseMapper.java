package com.quick.recording.gateway.mapper;

import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.entity.BaseEntity;
import org.mapstruct.*;

import java.util.List;

@MapperConfig(componentModel = "spring")
public interface BaseMapper {

    BaseEntity toEntity(BaseDto dto);

    BaseDto toDto(BaseEntity entity);

    List<BaseDto> toDtoList(List<BaseEntity> list);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BaseEntity toEntityWithoutNull(BaseDto dto, @MappingTarget BaseEntity entity);


    BaseEntity toEntity(BaseDto dto, @MappingTarget BaseEntity entity);
    
}
