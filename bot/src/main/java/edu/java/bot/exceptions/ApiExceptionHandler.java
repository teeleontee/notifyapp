package edu.java.bot.exceptions;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ApiResponse(description = "Request validation error")
    public ResponseEntity<ApiErrorResponse> handleApiException(Exception e) {
        String exceptionName = "Validation Error";
        ApiErrorResponse response = new ApiErrorResponse(
            e.getLocalizedMessage(),
            HttpStatus.BAD_REQUEST.toString(),
            exceptionName,
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
