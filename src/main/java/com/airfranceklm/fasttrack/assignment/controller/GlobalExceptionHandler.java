package com.airfranceklm.fasttrack.assignment.controller;


import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * GlobalExceptionHandler is a centralized exception handling component for the entire application.
 * It extends {@link ResponseEntityExceptionHandler} to customize the default handling of exceptions
 * and provides additional methods to handle specific exceptions, returning appropriate HTTP responses.
 *
 * <p>This class uses {@link ControllerAdvice} to intercept exceptions globally across all controllers.
 * It primarily handles method argument validation errors and {@link DateTimeParseException} instances,
 * responding with appropriate error messages in the response body.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles exceptions resulting from invalid method arguments annotated with {@code @Valid}.
     * When a request body validation fails, this method captures the details of the errors,
     * formats them into a list, and returns a {@code 400 Bad Request} status with the validation errors.
     *
     * @param ex      the {@link MethodArgumentNotValidException} thrown when method argument validation fails
     * @param headers the HTTP headers that should be returned in the response
     * @param status  the HTTP status that should be returned in the response
     * @param request the current {@link WebRequest} object
     * @return a {@link ResponseEntity} containing a map with the validation errors and an HTTP 400 status
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();

        // Extract the validation errors from the exception and format them into a list

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(field -> field.getField() + " " + field.getDefaultMessage())
                .collect(Collectors.toList());

        // Add the errors to the response body

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions related to date/time parsing, specifically {@link DateTimeParseException}.
     * This method is triggered when there is an issue with formatting or parsing date and time strings.
     * It returns a {@code 400 Bad Request} status with an appropriate error message indicating the wrong format.
     *
     * @param ex the {@link DateTimeParseException} thrown when a date or time string cannot be parsed
     * @return a {@link ResponseEntity} containing a map with the error message and an HTTP 400 status
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleFormat(DateTimeParseException ex) {
        Map<String, List<String>> body = new HashMap<>();
        body.put("errors", List.of("DateTime format is wrong"));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}