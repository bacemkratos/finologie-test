package com.finologie.banking.api.dtos;

import com.finologie.banking.api.enums.Currency;
import lombok.Data;

@Data
public class MoneyDepositDto {

    private Double amount;

    private Currency currency;
}
