package ua.com.foxminded.university.exception;

public class TimeslotTooShortException extends ServiceException {

    public TimeslotTooShortException(String message) {
        super(message);
    }
}
