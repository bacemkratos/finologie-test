package com.finologie.banking.api.services;

import com.finologie.banking.api.exception.WebBankingApiException;

public interface IbanValidationService {

    boolean validateIban(String iban, boolean isInternalIban) throws WebBankingApiException;
}
