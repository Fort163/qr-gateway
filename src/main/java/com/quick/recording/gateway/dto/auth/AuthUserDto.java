package com.quick.recording.gateway.dto.auth;

import com.quick.recording.gateway.config.validation.DateRange;
import com.quick.recording.resource.service.enumeration.AuthProvider;
import com.quick.recording.resource.service.enumeration.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AuthUserDto {

    @NotNull(message = "validation.uuid")
    private UUID uuid;
    private String fullName;
    private String firstName;
    private String lastName;
    private String userpic;
    @Email
    private String email;
    @NotNull(message = "validation.auth.username")
    private String username;
    private LocalDateTime lastVisit;
    @NotNull(message = "validation.auth.gender")
    private Gender gender;
    private String phoneNumber;
    @DateRange(pastYear = 18,message = "validation.auth.18.year.old")
    private LocalDate birthDay;
    @NotNull(message = "validation.auth.provider")
    private AuthProvider provider;

}
