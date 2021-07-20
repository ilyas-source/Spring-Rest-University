package ua.com.foxminded.university.exception;

public class EntityNotDeletedException extends RuntimeException {

    public EntityNotDeletedException(String message, Throwable cause) {
	super(message, cause);
    }

    public EntityNotDeletedException(String message) {
	super(message);
    }
}
