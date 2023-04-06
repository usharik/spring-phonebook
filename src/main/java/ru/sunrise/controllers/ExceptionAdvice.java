package ru.sunrise.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.sunrise.controllers.dto.ErrorDto;
import ru.sunrise.resources.RestBadArgumentException;
import ru.sunrise.resources.RestNotFoundException;

import java.util.Map;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        return new ModelAndView(
                "not_found",
                Map.of("message", ex.getLocalizedMessage()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorDto handleRestNotFoundException(RestNotFoundException ex) {
        return new ErrorDto(ex.getLocalizedMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorDto handleRestBadArgumentException(RestBadArgumentException ex) {
        return new ErrorDto(ex.getLocalizedMessage());
    }
}
