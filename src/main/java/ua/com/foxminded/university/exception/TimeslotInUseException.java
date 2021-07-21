package ua.com.foxminded.university.exception;

public class TimeslotInUseException extends RuntimeException {

    public TimeslotInUseException(String message) {
	super(message);
    }
}