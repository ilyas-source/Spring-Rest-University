package ua.com.foxminded.university.exception;

public class TimeslotInUseException extends ServiceException {

    public TimeslotInUseException(String message) {
	super(message);
    }
}