package ua.com.foxminded.university.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class GroupBusyException extends ServiceException {

    public GroupBusyException(String message) {
        super(message);
    }
}
