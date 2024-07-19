package com.finologie.banking.api.exception;

import lombok.Getter;

@Getter
public class WebBankingApiFraudException extends WebBankingApiException {

    private String details;

    public WebBankingApiFraudException(String message, String details, Throwable cause) {

        super(message, cause);
        this.details = details;
    }

    public WebBankingApiFraudException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebBankingApiFraudException(String message) {
        super(message);
    }

    public WebBankingApiFraudException() {
        super();
    }
}
