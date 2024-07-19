package com.finologie.banking.api.services;

import com.finologie.banking.api.entites.BankAccount;
import com.finologie.banking.api.entites.BankTransaction;
import com.finologie.banking.api.entites.Payment;
import com.finologie.banking.api.enums.TransactionType;
import com.finologie.banking.api.exception.WebBankingApiException;

public interface BankTransactionService {

    void processTransaction(BankTransaction giverTransaction) throws WebBankingApiException;

    BankTransaction createPaymentTransaction(Payment payment, BankAccount bankAccount, TransactionType transactionType, boolean external) throws WebBankingApiException;


    BankTransaction createInnerPaymentTransaction(Payment payment, BankAccount giverAccount, TransactionType transactionType) throws WebBankingApiException;

    BankTransaction createExternalPaymentTransaction(Payment payment, String bankAccount, TransactionType transactionType);
}
