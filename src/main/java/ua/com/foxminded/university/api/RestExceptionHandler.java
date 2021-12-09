package ua.com.foxminded.university.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.foxminded.university.exception.ClassroomInvalidCapacityException;

@RestControllerAdvice("ua.com.foxminded.university.api")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(EntityNotUniqueException.class)
//    protected ResponseEntity<Object> handleConflicts(Exception exception, WebRequest request) {
//        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
//                HttpStatus.CONFLICT, request);
//    }

    @ExceptionHandler(ClassroomInvalidCapacityException.class)
    protected ResponseEntity<Object> handleBadRequests(Exception exception, WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }
}
