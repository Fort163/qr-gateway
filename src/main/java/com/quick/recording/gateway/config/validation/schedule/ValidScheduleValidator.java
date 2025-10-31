package com.quick.recording.gateway.config.validation.schedule;

import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class ValidScheduleValidator implements ConstraintValidator<ValidSchedule, ScheduleDto> {

    @Override
    public boolean isValid(ScheduleDto scheduleDto, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(scheduleDto.getClockFrom()) || Objects.isNull(scheduleDto.getClockTo())){
            return true;
        }
        return scheduleDto.getClockFrom().isBefore(scheduleDto.getClockTo());
    }

}
