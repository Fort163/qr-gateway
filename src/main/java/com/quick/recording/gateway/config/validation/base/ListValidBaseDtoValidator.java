package com.quick.recording.gateway.config.validation.base;

import com.quick.recording.gateway.dto.BaseDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Objects;

public class ListValidBaseDtoValidator implements ConstraintValidator<ValidBaseDto, List<?>> {

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return true;
        }
        boolean result = true;
        for (Object obj : list){
            if(BaseDto.class.isAssignableFrom(obj.getClass())) {
                BaseDto dto = (BaseDto) obj;
                if (Objects.isNull(dto.getUuid())) {
                    result = false;
                    break;
                }
            }
            else {
                result = false;
                break;
            }
        }
        return result;
    }

}
