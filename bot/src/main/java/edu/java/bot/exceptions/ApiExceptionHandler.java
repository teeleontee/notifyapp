package edu.java.bot.exceptions;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice public class ApiExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class) @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ApiResponse(description = "Request validation error")
    public ResponseEntity<ApiErrorResponse> handleApiException(HttpMessageNotReadableException e) {
        String exceptionName = "Validation Error";
        String errorCode = String.valueOf(HttpStatus.BAD_REQUEST);
        return getGenericApiErrorResponseEntity(e, errorCode, exceptionName);
    }

    @ExceptionHandler(NoResourceFoundException.class) @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ApiResponse(description = "Resource not found")
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(NoResourceFoundException e) {
        String exceptionName = "Request resource was not found";
        String errorCode = String.valueOf(HttpStatus.NOT_FOUND);
        return getGenericApiErrorResponseEntity(e, errorCode, exceptionName);
    }

    @ExceptionHandler(NullPointerException.class) @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(description = "server error")
    public ResponseEntity<ApiErrorResponse> handleServerException(NullPointerException e) {
        String exceptionName = "Internal server error occurred";
        String errorCode = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
        return getGenericApiErrorResponseEntity(e, errorCode, exceptionName);
    }

    @NotNull private static ResponseEntity<ApiErrorResponse> getGenericApiErrorResponseEntity(
        Exception e, String errorCode, String exceptionName
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
            e.getLocalizedMessage(),
            errorCode,
            exceptionName,
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
