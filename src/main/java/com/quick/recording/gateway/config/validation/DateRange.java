package com.quick.recording.gateway.config.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRange {

    int pastYear() default -1;
    int pastMonth() default -1;
    int pastDay() default -1;
    int featureYear() default -1;
    int featureMonth() default -1;
    int featureDay() default -1;

    String message() default "Date not valid";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
