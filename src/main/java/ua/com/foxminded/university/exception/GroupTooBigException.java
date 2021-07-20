package ua.com.foxminded.university.exception;

public class GroupTooBigException extends RuntimeException {

    public GroupTooBigException(String message) {
	super(message);
    }
}
