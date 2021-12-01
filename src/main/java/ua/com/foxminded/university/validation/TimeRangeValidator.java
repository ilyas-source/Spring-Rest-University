package ua.com.foxminded.university.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import java.time.LocalTime;

import static javax.validation.constraintvalidation.ValidationTarget.PARAMETERS;

@SupportedValidationTarget(PARAMETERS)
public class TimeRangeValidator implements ConstraintValidator<TimeRangeConstraint, Object[]> {

    @Override
    public void initialize(TimeRangeConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        return ((LocalTime) value[0]).isBefore((LocalTime) value[1]);
    }
}
