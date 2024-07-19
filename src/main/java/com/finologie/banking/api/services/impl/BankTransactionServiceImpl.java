package com.finologie.banking.api.services.impl;

import com.finologie.banking.api.entites.BankAccount;
import com.finologie.banking.api.entites.BankTransaction;
import com.finologie.banking.api.entites.Payment;
import com.finologie.banking.api.enums.TransactionType;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.repositories.BankTransactionRepository;
import com.finologie.banking.api.services.BankAccountService;
import com.finologie.banking.api.services.BankTransactionService;
import com.finologie.banking.api.services.CurrencyConversionService;
import org.springframework.stereotype.Service;

@Service
public class BankTransactionServiceImpl implements BankTransactionService {

    private final BankTransactionRepository bankTransactionRepository;
    private final BankAccountService bankAccountService;
    private final CurrencyConversionService currencyConversionService;

    public BankTransactionServiceImpl(BankTransactionRepository bankTransactionRepository, BankAccountService bankAccountService, CurrencyConversionService currencyConversionService) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.bankAccountService = bankAccountService;
        this.currencyConversionService = currencyConversionService;
    }

    @Override
    public void processTransaction(BankTransaction bankTransaction) throws WebBankingApiException {
        if (!bankTransaction.isExternal())
            bankAccountService.updateBankBalance(bankTransaction);


    }

    @Override
    public BankTransaction createPaymentTransaction(Payment payment, BankAccount bankAccount, TransactionType transactionType, boolean external) throws WebBankingApiException {
        BankTransaction giverTransaction = new BankTransaction();
        giverTransaction.setPayment(payment);
        giverTransaction.setDescription(payment.getCommunication());
        giverTransaction.setAmount(currencyConversionService.convertCurrency(payment.getCurrency().toString(),
                bankAccount.getCurrency().toString(),
                payment.getAmount()));
        giverTransaction.setCurrency(bankAccount.getCurrency());
        giverTransaction.setBankAccount(bankAccount);
        giverTransaction.setType(transactionType);
        giverTransaction.setExternal(external);
        return bankTransactionRepository.save(giverTransaction);
    }

    @Override
    public BankTransaction createInnerPaymentTransaction(Payment payment, BankAccount account, TransactionType transactionType) throws WebBankingApiException {
        return createPaymentTransaction(payment, account, transactionType, false);
    }

    @Override
    public BankTransaction createExternalPaymentTransaction(Payment payment, String bankAccount, TransactionType transactionType) {
        BankTransaction giverTransaction = new BankTransaction();
        giverTransaction.setPayment(payment);
        giverTransaction.setDescription(payment.getCommunication());
        giverTransaction.setAmount(payment.getAmount());
        giverTransaction.setCurrency(payment.getCurrency());
        giverTransaction.setBankAccount(null);
        giverTransaction.setType(transactionType);
        giverTransaction.setExternal(true);
        giverTransaction.setExternalIban(bankAccount);
        return bankTransactionRepository.save(giverTransaction);
    }
}
