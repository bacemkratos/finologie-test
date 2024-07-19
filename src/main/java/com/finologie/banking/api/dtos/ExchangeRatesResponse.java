package com.finologie.banking.api.dtos;

import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRatesResponse {

    private String base;
    private Map<String, Double> rates;
    private String date;


}
