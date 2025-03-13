package com.quick.recording.gateway.config.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class DateRangeValidator implements ConstraintValidator<DateRange, LocalDate> {

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        Map<String, Object> attributes = ((ConstraintValidatorContextImpl) constraintValidatorContext).getConstraintDescriptor().getAttributes();
        LocalDate from = getDate(attributes, "past");
        LocalDate to = getDate(attributes, "feature");
        if (Objects.isNull(localDate)) {
            return true;
        }
        if (Objects.nonNull(from) && Objects.nonNull(to)) {
            return from.isBefore(localDate) && to.isAfter(localDate);
        }
        if (Objects.nonNull(from)) {
            return from.isAfter(localDate);
        }
        if (Objects.nonNull(to)) {
            return to.isBefore(localDate);
        }
        return true;
    }

    private LocalDate getDate(Map<String, Object> attributes, String type) {
        LocalDate date = null;
        Integer year = (Integer) attributes.get(type + "Year");
        Integer month = (Integer) attributes.get(type + "Month");
        Integer day = (Integer) attributes.get(type + "Day");
        if (year != -1 || month != -1 || day != -1) {
            switch (type) {
                case "past": {
                    date = LocalDate.now();
                    if (year != -1) date = date.minusYears(year);
                    if (month != -1) date = date.minusMonths(month);
                    if (day != -1) date = date.minusDays(day);
                    return date;
                }
                case "feature": {
                    date = LocalDate.now();
                    if (year != -1) date = date.plusYears(year);
                    if (month != -1) date = date.plusMonths(month);
                    if (day != -1) date = date.plusDays(day);
                    return date;
                }
            }
        }
        return date;
    }

}
