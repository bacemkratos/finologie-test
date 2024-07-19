package com.finologie.banking.api.services;

import com.finologie.banking.api.dtos.MoneyDepositDto;
import com.finologie.banking.api.entites.BankAccount;
import com.finologie.banking.api.entites.BankTransaction;
import com.finologie.banking.api.exception.WebBankingApiException;

import java.util.Optional;
import java.util.Set;

public interface BankAccountService {

    BankAccount openAccount(BankAccount entity) throws WebBankingApiException;

    Optional<BankAccount> findByIbanNumber(String ibanNumber);

    void updateBankBalance(BankTransaction savedBankTransaction) throws WebBankingApiException;

    Boolean depositMoney(String bankAccountIban, MoneyDepositDto moneyDepositDto) throws WebBankingApiException;

    Set<BankAccount> getConnectedUserBankAccounts() throws WebBankingApiException;
}
