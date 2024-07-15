package com.finologie.banking.api.exception;

import lombok.Getter;

@Getter
public class WebBankingApiException extends  Exception {

    private  String details;

    public WebBankingApiException(String message,String details, Throwable cause) {

        super(message, cause);
        this.details = details;
    }

    public WebBankingApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebBankingApiException(String message) {
        super(message);
    }

    public WebBankingApiException() {
        super();
    }
}
