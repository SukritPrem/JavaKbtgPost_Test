package com.kbtg.bootcamp.posttest.exception;

import jakarta.servlet.ServletException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    public ApiExceptionHandler(){

    }
    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({Status200Exception.class
    })
    public ResponseEntity<Object> handleStatus200Exception(Status200Exception e)
    {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                e.getMessage(), HttpStatus.OK, ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    @ExceptionHandler({ServerInternalErrorException.class})
    public ResponseEntity<Object> handleServerInternalErrorException(NotFoundException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleInvalidType(MethodArgumentTypeMismatchException e) {
        String errorMessage = "Invalid parameter type: " + e.getName();
        ApiErrorResponse errorResponse = new ApiErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, ZonedDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        ApiErrorResponse errorResponse = new ApiErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, ZonedDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthenticationServiceException.class, ServletException.class})
    public ResponseEntity<String> handleAuthenticationFailure(Exception e) {
        if (e instanceof AuthenticationServiceException || e instanceof ServletException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred");
        }
    }
    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(PSQLException ex) {
        // Check if the error message contains information about a duplicate key violation
        if (ex.getMessage().contains("duplicate key value violates unique constraint")) {
            // Customize the error message as per your requirement
            String errorMessage = "The provided ticket value already exists.";
            // Return a ResponseEntity with the error message and status code 409 (Conflict)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
        // If the exception is not related to duplicate key violation, handle it accordingly
        // You can add additional catch blocks for other types of exceptions if needed

        // If the exception doesn't match any known error condition, return a generic error message
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}
