package com.quick.recording.gateway.config.validation.base;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {ValidBaseDtoValidator.class, ListValidBaseDtoValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBaseDto {

    String message() default "The object does not exist or does not have an identifier";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
