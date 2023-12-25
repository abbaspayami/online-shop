package com.abbas.securityservice.config;

import com.abbas.securityservice.domain.model.ErrorMessage;
import com.abbas.securityservice.exception.InvalidDateException;
import com.abbas.securityservice.exception.NotFoundException;
import com.abbas.securityservice.exception.TokenHasBeenRevokedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(Throwable exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorMessage errorMessage = ErrorMessage.build(exception.getMessage(), exception.getClass().getSimpleName());
        return new ResponseEntity<>(errorMessage, status);
    }

    /**
     * Handles ObjectNotFoundException
     *
     * @param exception catch Exception
     * @return An ExceptionDto
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(Exception exception) {
        ErrorMessage errorMessage = ErrorMessage.build(exception.getMessage(), exception.getClass().getSimpleName());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenHasBeenRevokedException.class)
    public ResponseEntity<ErrorMessage> handleTokenHasBeenRevokedException(Exception exception) {
        ErrorMessage errorMessage = ErrorMessage.build(exception.getMessage(), exception.getClass().getSimpleName());
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorMessage> handleInvalidDateException(Exception exception) {
        ErrorMessage errorMessage = ErrorMessage.build(exception.getMessage(), exception.getClass().getSimpleName());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_ACCEPTABLE);
    }

}
