package com.quick.recording.gateway.dto.auth;

import com.quick.recording.resource.service.enumeration.AuthProvider;
import com.quick.recording.resource.service.enumeration.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchUserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Gender gender;
    private String phoneNumber;
    private LocalDate birthDay;
    private AuthProvider provider;

}
