package com.finologie.banking.api.dtos;

import com.finologie.banking.api.enums.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountCreationDto {


    private Currency currency;


    private String AccountName;


}
