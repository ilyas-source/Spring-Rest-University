package ua.com.foxminded.university.exception;

public class EntityInUseException extends ServiceException {

    public EntityInUseException(String message) {
	super(message);
    }
}
