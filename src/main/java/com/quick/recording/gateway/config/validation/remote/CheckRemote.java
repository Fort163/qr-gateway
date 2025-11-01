package com.quick.recording.gateway.config.validation.remote;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckRemoteValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRemote {

    Class<? extends SmartDto> typeDto();

    String message() default "Remote service UUID not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
