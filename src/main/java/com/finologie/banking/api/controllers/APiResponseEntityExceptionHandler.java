package com.finologie.banking.api.controllers;

import com.finologie.banking.api.dtos.ApiErrorResponse;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.exception.WebBankingApiFraudException;
import com.finologie.banking.api.services.AuthAndUserService;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class APiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {







    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ WebBankingApiException.class })
    public ResponseEntity<ApiErrorResponse> handleCustomApiException(
            WebBankingApiException ex,WebRequest request) {
               return   ResponseEntity.internalServerError().body( ApiErrorResponse.builder()
                       .details(ex.getDetails()).message(ex.getMessage())
                       .statusCode(55).
                       build());

    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(ConstraintViolationException ex,  WebRequest request) {


        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String fieldName =  error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(
                ApiErrorResponse.builder()
                        .message("one or multiple fields didn't pass the validation criteria").details(errors).statusCode(40).build()
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ JwtException.class })
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            JwtException ex,WebRequest request) {
        return   ResponseEntity.status(HttpStatus.FORBIDDEN).body( ApiErrorResponse.builder()
                .details(ex.getMessage()).message("Token or privilege problem")
                .statusCode(48).
                build());

    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(
            BadCredentialsException ex,WebRequest request) {
        return   ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( ApiErrorResponse.builder()
                .details(ex.getMessage()).message("Password or username is wrong")
                .statusCode(41).
                build());

    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,WebRequest request) {
        return   ResponseEntity.internalServerError().body( ApiErrorResponse.builder()
                .details(ex.getMessage()).message("Something went wrong on server side")
                .statusCode(50).
                build());

    }


}
