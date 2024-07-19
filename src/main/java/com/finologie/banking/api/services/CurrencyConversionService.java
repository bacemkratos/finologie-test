package com.finologie.banking.api.services;

public interface CurrencyConversionService {
    Double convertCurrency(String fromCurrency, String toCurrency, Double amount);
}
