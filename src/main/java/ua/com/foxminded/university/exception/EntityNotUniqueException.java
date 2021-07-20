package ua.com.foxminded.university.exception;

public class EntityNotUniqueException extends RuntimeException {

    public EntityNotUniqueException(String message) {
	super(message);
    }
}
