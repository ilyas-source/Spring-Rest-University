package ua.com.foxminded.university.exception;

public class EntityNotFoundException extends ServiceException {

    public EntityNotFoundException(String message) {
	super(message);
    }
}
