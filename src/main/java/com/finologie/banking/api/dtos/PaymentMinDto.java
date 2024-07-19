package com.finologie.banking.api.dtos;

import com.finologie.banking.api.enums.Currency;
import lombok.Data;

@Data
public class PaymentMinDto {


    private Long id;

    private Double amount;


    private Currency currency;


    private BankAccountMinDto giverAccount;


    private String beneficiaryAccountNumber;

    private String beneficiaryName;


    private String communication;


    private boolean excutionStatus;


}
