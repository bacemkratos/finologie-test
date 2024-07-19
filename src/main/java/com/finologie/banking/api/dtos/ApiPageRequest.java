package com.finologie.banking.api.dtos;

import lombok.Data;

@Data
public class ApiPageRequest {
    private int size;
    private int pageNumber;
}
