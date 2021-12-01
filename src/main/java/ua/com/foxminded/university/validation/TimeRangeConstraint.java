package ua.com.foxminded.university.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, CONSTRUCTOR, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = TimeRangeValidator.class)
@Documented
public @interface TimeRangeConstraint {
    String message () default "{timerange.invalid}";

    Class<?>[] groups () default {};

    Class<? extends Payload>[] payload () default {};
}