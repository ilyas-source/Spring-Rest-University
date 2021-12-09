package ua.com.foxminded.university.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class EntityNotUniqueException extends ServiceException {

    public EntityNotUniqueException(String message) {
        super(message);
    }
}
