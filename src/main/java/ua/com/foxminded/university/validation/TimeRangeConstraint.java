package ua.com.foxminded.university.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = TimeRangeValidator.class)
@Documented
public @interface TimeRangeConstraint {
    String message() default "{timerange.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}