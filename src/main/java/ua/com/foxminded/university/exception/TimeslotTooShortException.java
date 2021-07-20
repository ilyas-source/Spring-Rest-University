package ua.com.foxminded.university.exception;

public class TimeslotTooShortException extends RuntimeException {

    public TimeslotTooShortException(String message) {
	super(message);
    }
}
