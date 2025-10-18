package com.quick.recording.gateway.config.validation.base;

import com.quick.recording.gateway.dto.BaseDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class ValidBaseDtoValidator implements ConstraintValidator<ValidBaseDto, BaseDto> {

    @Override
    public boolean isValid(BaseDto dto, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(dto)){
            return true;
        }
        return Objects.nonNull(dto.getUuid());
    }

}
