package ua.com.foxminded.university.exception;

public class EntityNotCreatedException extends RuntimeException {

    public EntityNotCreatedException(String message, Throwable cause) {
	super(message, cause);
    }

    public EntityNotCreatedException(String message) {
	super(message);
    }
}
