package ae.mashreq.internal.meetup.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handle(MethodArgumentNotValidException exception) {
        return error(exception.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList()));
    }

    private static Map error(Object message) {
        return Collections.singletonMap("errors", message);
    }
}