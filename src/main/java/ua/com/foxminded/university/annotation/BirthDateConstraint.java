package ua.com.foxminded.university.annotation;

import ua.com.foxminded.university.validator.BirthDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BirthDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDateConstraint {
    String message() default "Student age must not be less than 14";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
