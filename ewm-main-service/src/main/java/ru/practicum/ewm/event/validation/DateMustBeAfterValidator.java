package ru.practicum.ewm.event.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateMustBeAfterValidator implements ConstraintValidator<DateMustBeAfter, LocalDateTime> {

    private int hours;

    public void initialize(DateMustBeAfter annotation) {
        hours = annotation.hours();
    }

    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        LocalDateTime date = LocalDateTime.now().plusHours(hours);
        boolean valid = true;
        if (value != null) {
            if (!value.isAfter(date) && !value.isEqual(date)) {
                valid = false;
            }
        }
        return valid;
    }
}
