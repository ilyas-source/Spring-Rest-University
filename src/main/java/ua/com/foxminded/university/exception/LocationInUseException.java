package ua.com.foxminded.university.exception;

public class LocationInUseException extends RuntimeException {

    public LocationInUseException(String message) {
	super(message);
    }
}
