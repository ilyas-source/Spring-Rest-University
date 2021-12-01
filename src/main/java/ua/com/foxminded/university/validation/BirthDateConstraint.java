package ua.com.foxminded.university.validation;

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
    String message() default "{student.tooyoung}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int age() default 14;
}
