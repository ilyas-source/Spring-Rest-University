package ua.com.foxminded.university.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends ServiceException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
