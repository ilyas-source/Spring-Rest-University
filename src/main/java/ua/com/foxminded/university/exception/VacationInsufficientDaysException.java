package ua.com.foxminded.university.exception;

public class VacationInsufficientDaysException extends RuntimeException {

    public VacationInsufficientDaysException(String message) {
	super(message);
    }
}