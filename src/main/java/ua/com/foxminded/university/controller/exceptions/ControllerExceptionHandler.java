package ua.com.foxminded.university.controller.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ua.com.foxminded.university.exception.ServiceException;

@ControllerAdvice("ua.com.foxminded.university.controller")
public class ControllerExceptionHandler {

    public static final String EXCEPTION_VIEW = "exceptions/error";

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    public ModelAndView handleServiceException(Exception e) {
        logger.debug("Handling {} - [{}]", e.getClass(), e.getMessage());
        logger.error(e.getMessage(), e);
        return prepareModel(e, EXCEPTION_VIEW);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception e) {
        logger.debug("Handling {} - [{}]", e.getClass(), e.getMessage());
        logger.error(e.getMessage(), e);
        return prepareModel(e, EXCEPTION_VIEW);
    }

    private ModelAndView prepareModel(Exception e, String view) {
        ModelAndView modelView = new ModelAndView(view);
        modelView.addObject("title", e.getClass().getSimpleName());
        modelView.addObject("message", e.getMessage());
        return modelView;
    }


}
