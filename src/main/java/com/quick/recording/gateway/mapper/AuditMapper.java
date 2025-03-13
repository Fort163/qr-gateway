package com.quick.recording.gateway.mapper;

import com.quick.recording.gateway.dto.AuditDto;
import com.quick.recording.gateway.entity.AuditEntity;
import org.mapstruct.*;

import java.util.List;

@MapperConfig(componentModel = "spring", uses = {BaseMapper.class})
public interface AuditMapper {

    AuditEntity toEntity(AuditDto dto);

    AuditDto toDto(AuditEntity entity);

    List<AuditDto> toDtoList(List<AuditEntity> list);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AuditEntity toEntityWithoutNull(AuditDto dto, @MappingTarget AuditEntity entity);


    AuditEntity toEntity(AuditDto dto, @MappingTarget AuditEntity entity);
    
}
