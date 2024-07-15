package com.finologie.banking.api.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiErrorResponse {

    private int statusCode;
    private String message;
    private Object details;
}
