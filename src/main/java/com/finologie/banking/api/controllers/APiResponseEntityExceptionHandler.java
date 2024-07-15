package com.finologie.banking.api.controllers;

import com.finologie.banking.api.dtos.ApiErrorResponse;
import com.finologie.banking.api.exception.WebBankingApiException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ WebBankingApiException.class })
    public ResponseEntity<ApiErrorResponse> handleCustomApiException(
            WebBankingApiException ex,WebRequest request) {
               return   ResponseEntity.badRequest().body( ApiErrorResponse.builder()
                       .details(ex.getDetails()).message(ex.getMessage())
                       .statusCode(400).
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
                        .message("one or multiple fields didn't pass the validation criteria").details(errors).statusCode(400).build()
        );
    }





}
