package ua.com.foxminded.university.validation;

import ua.com.foxminded.university.model.Timeslot;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeRangeValidator implements ConstraintValidator<TimeRangeConstraint, Timeslot> {

    @Override
    public void initialize(TimeRangeConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Timeslot timeslot, ConstraintValidatorContext constraintValidatorContext) {
        return timeslot.getEndTime().isAfter(timeslot.getBeginTime());
    }
}
