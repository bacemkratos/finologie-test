package com.finologie.banking.api.dtos;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class IbanValidationResponse {
    private boolean valid;
    private String iban;
    private List<String> messages;
    private HashMap<String, String> bankData;
    private HashMap<String, String> checkResults;
}
