package ua.com.foxminded.university.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<BirthDateConstraint, LocalDate> {

    private int age;

    @Override
    public void initialize(BirthDateConstraint constraintAnnotation) {
        this.age=constraintAnnotation.age();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date.isBefore(LocalDate.now().minusYears(age));
    }
}
