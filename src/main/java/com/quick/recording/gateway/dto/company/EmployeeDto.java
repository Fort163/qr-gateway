package com.quick.recording.gateway.dto.company;

import com.quick.recording.gateway.config.validation.base.ValidBaseDto;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import com.quick.recording.gateway.enumerated.CompanyHierarchyEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;
import java.util.UUID;

@Data
@RedisHash("Employee")
public class EmployeeDto extends SmartDto {

    @NotNull(
            message = "{validation.employee.auth.uuid}",
            groups = {Put.class, Post.class}
    )
    private UUID authId;
    @NotNull(
            message = "{validation.employee.profession.not.null}",
            groups = {Put.class, Post.class}
    )
    @ValidBaseDto(
            message = "{validation.base.dto.default}",
            groups = {Post.class, Put.class, Patch.class}
    )
    private ProfessionDto profession;
    @NotNull(
            message = "{validation.company.not.null}",
            groups = {Put.class, Post.class}
    )
    @ValidBaseDto(
            message = "{validation.base.dto.default}",
            groups = {Post.class, Put.class, Patch.class}
    )
    private BaseDto company;
    @NotEmpty(
            message = "{validation.employee.permissions.not.empty}",
            groups = {Put.class, Post.class}
    )
    private List<CompanyHierarchyEnum> permissions;
    @NotEmpty(
            message = "{validation.employee.services.not.empty}",
            groups = {Put.class, Post.class}
    )
    @ValidBaseDto(
            message = "{validation.list.base.dto.default}",
            groups = {Post.class, Put.class, Patch.class}
    )
    private List<BaseDto> services;

}
