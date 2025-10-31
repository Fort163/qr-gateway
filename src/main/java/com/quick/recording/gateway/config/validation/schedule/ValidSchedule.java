package com.quick.recording.gateway.config.validation.schedule;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidScheduleValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSchedule {

    String message() default "Schedule not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
