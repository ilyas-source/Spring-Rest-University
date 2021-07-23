package ua.com.foxminded.university.exception;

public class VacationInsufficientDaysException extends ServiceException {

    public VacationInsufficientDaysException(String message) {
	super(message);
    }
}