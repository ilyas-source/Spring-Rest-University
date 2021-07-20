package ua.com.foxminded.university.exception;

public class GroupBusyException extends RuntimeException {

    public GroupBusyException(String message) {
	super(message);
    }
}
