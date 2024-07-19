package com.finologie.banking.api.services.impl;

import com.finologie.banking.api.dtos.ExchangeRatesResponse;
import com.finologie.banking.api.services.CurrencyConversionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {


    private final RestTemplate restTemplate;
    private final String currencyConverterApiUrl;
    private Map<String, Double> exchangeRates = new HashMap<>();


    public CurrencyConversionServiceImpl(RestTemplate restTemplate,
                                         @Value("${currency.api.url}") String currencyConverterApiUrl) {
        this.restTemplate = restTemplate;
        this.currencyConverterApiUrl = currencyConverterApiUrl;
    }

    @PostConstruct
    public void fetchExchangeRates() {
        String url = currencyConverterApiUrl;
        ExchangeRatesResponse response = restTemplate.getForObject(url, ExchangeRatesResponse.class);
        if (response != null && response.getRates() != null) {
            exchangeRates = response.getRates();
            exchangeRates.put("EUR", 1.0); // Adding base currency (Euro) as 1.0
        }
    }

    @Override
    public Double convertCurrency(String fromCurrency, String toCurrency, Double amount) {
        Double fromRate = exchangeRates.get(fromCurrency);
        Double toRate = exchangeRates.get(toCurrency);

        if (fromRate != null && toRate != null) {
            return amount * (toRate / fromRate);
        }
        return null;
    }


}
