package com.finologie.banking.api.dtos;

import com.finologie.banking.api.enums.Currency;
import lombok.Data;

@Data
public class PaymentCreationDto {


    private Double amount;


    private Currency currency;


    private BankAccountIbanOnlyDto giverAccount;


    private String beneficiaryAccountNumber;

    private String beneficiaryName;


    private String communication;


}
