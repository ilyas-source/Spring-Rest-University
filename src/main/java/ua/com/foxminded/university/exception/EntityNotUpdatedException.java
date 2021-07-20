package ua.com.foxminded.university.exception;

public class EntityNotUpdatedException extends RuntimeException {

    public EntityNotUpdatedException(String message, Throwable cause) {
	super(message, cause);
    }

    public EntityNotUpdatedException(String message) {
	super(message);
    }
}
