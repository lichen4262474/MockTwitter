package com.cooksystem.socialmedia.controller.controlleradvice;

import com.cooksystem.socialmedia.dto.ErrorDto;
import com.cooksystem.socialmedia.exceptions.BadRequestException;
import com.cooksystem.socialmedia.exceptions.NotAuthorizedException;
import com.cooksystem.socialmedia.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice(basePackages = {"com.cooksystem.socialmedia.controller"})
@ResponseBody
public class MyControllerAdvice {
    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handleNotAuthorizedException(HttpServletRequest request, NotAuthorizedException notAuthorizedException){
        return new ErrorDto(notAuthorizedException.getMessage());

    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(HttpServletRequest request, NotFoundException notFoundException){
        return new ErrorDto(notFoundException.getMessage());

    }
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequestException(HttpServletRequest request, BadRequestException badRequestException){
        return new ErrorDto(badRequestException.getMessage());

    }
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleRequestBodyValidationException(HttpServletRequest request,MethodArgumentNotValidException ex){
        // Concatenate all error messages into one string
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        // Return ErrorDto with the concatenated error message
        return new ErrorDto(errorMessage);
    }
}
