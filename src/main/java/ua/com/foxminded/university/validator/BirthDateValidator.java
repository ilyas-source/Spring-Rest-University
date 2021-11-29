package ua.com.foxminded.university.validator;

import ua.com.foxminded.university.annotation.BirthDateConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<BirthDateConstraint, LocalDate> {
    @Override
    public void initialize(BirthDateConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date.isBefore(LocalDate.now().minusYears(14));
    }
}
