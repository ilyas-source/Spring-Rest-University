package ua.com.foxminded.university.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class VacationInsufficientDaysException extends ServiceException {

    public VacationInsufficientDaysException(String message) {
        super(message);
    }
}