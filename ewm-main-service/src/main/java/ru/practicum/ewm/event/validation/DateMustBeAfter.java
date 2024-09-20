package ru.practicum.ewm.event.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateMustBeAfterValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateMustBeAfter {

    String message() default "date must be {value} later after now";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int hours();
}
