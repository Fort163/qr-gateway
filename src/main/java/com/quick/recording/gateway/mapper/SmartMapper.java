package com.quick.recording.gateway.mapper;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.entity.SmartEntity;
import org.mapstruct.*;

import java.util.List;

@MapperConfig(componentModel = "spring", uses = {AuditMapper.class})
public interface SmartMapper {

    SmartEntity toEntity(SmartDto dto);

    SmartDto toDto(SmartEntity entity);

    List<SmartDto> toDtoList(List<SmartEntity> list);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SmartEntity toEntityWithoutNull(SmartDto dto, @MappingTarget SmartEntity entity);


    SmartEntity toEntity(SmartDto dto, @MappingTarget SmartEntity entity);

}
