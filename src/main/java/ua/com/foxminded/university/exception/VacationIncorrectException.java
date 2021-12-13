package ua.com.foxminded.university.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class VacationIncorrectException extends ServiceException {

    public VacationIncorrectException(String message) {
        super(message);
    }
}