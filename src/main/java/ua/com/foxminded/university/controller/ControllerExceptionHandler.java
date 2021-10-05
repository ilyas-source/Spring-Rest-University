package ua.com.foxminded.university.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllerExceptionHandler {

    public static final String EXCEPTION_VIEW = "exceptions/error";

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception e) {
        return prepareModel(e, EXCEPTION_VIEW);
    }

    private ModelAndView prepareModel(Exception e, String view) {
        ModelAndView modelView = new ModelAndView(view);
        modelView.addObject("title", e.getClass().getSimpleName());
        modelView.addObject("message", e.getMessage());
        return modelView;
    }


}
