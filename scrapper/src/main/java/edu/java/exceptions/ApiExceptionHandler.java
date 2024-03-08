package edu.java.exceptions;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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
