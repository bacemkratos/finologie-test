package com.finologie.banking.api.dtos;

import com.finologie.banking.api.enums.BankAccountStatus;
import com.finologie.banking.api.enums.Currency;
import lombok.Data;

@Data
public class BankAccountMinDto {


    private String ibanNumber;

    private Double balanceAmount;


    private Currency currency;


    private String AccountName;


    private BankAccountStatus status;


}
