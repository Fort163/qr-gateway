package com.quick.recording.gateway.dto.user;

import com.quick.recording.gateway.annotations.RemoteFill;
import com.quick.recording.gateway.config.validation.remote.CheckRemote;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.auth.AuthUserDto;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.company.EmployeeDto;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Data
@RedisHash("UserInfo")
public class UserInfoDto extends SmartDto {

    @NotNull(
            message = "{validation.auth.uuid.user}",
            groups = {Put.class, Post.class}
    )
    @CheckRemote(
            message = "{validation.check.remote.uuid}",
            typeDto = AuthUserDto.class,
            groups = {Post.class, Patch.class, Put.class}
    )
    private UUID userId;
    @CheckRemote(
            message = "{validation.check.remote.uuid}",
            typeDto = CompanyDto.class,
            groups = {Post.class, Patch.class, Put.class}
    )
    private UUID companyId;
    @CheckRemote(
            message = "{validation.check.remote.uuid}",
            typeDto = EmployeeDto.class,
            groups = {Post.class, Patch.class, Put.class}
    )
    private UUID employeeId;
    @RemoteFill(
            fieldName = "userId",
            typeDto = AuthUserDto.class
    )
    private AuthUserDto user;
    @RemoteFill(
            fieldName = "companyId",
            typeDto = CompanyDto.class
    )
    private CompanyDto company;
    @RemoteFill(
            fieldName = "employeeId",
            typeDto = EmployeeDto.class
    )
    private EmployeeDto employee;

}
