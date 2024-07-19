package com.finologie.banking.api.services.impl;

import com.finologie.banking.api.dtos.MoneyDepositDto;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.entites.Balance;
import com.finologie.banking.api.entites.BankAccount;
import com.finologie.banking.api.entites.BankTransaction;
import com.finologie.banking.api.enums.BalanceOperation;
import com.finologie.banking.api.enums.BalanceType;
import com.finologie.banking.api.enums.BankAccountStatus;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.repositories.AppUserRepository;
import com.finologie.banking.api.repositories.BalanceRepository;
import com.finologie.banking.api.repositories.BankAccountRepository;
import com.finologie.banking.api.services.AuthAndUserService;
import com.finologie.banking.api.services.BankAccountService;
import com.finologie.banking.api.services.CurrencyConversionService;
import com.finologie.banking.api.utils.IbanUtil;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final CurrencyConversionService currencyConversionService;

    private final AuthAndUserService authAndUserService;

    private final AppUserRepository appUserRepository;

    private final BalanceRepository balanceRepository;


    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, AuthAndUserService authAndUserService,  CurrencyConversionService currencyConversionService, AppUserRepository appUserRepository, BalanceRepository balanceRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.authAndUserService = authAndUserService;
        this.currencyConversionService = currencyConversionService;
        this.appUserRepository = appUserRepository;
        this.balanceRepository = balanceRepository;
    }


    @Override
    public BankAccount openAccount(BankAccount entity) throws WebBankingApiException {

        //get connected user
        AppUser user = authAndUserService.getConnectedUser();

        //init bank account properties
        entity.setStatus(BankAccountStatus.ACTIVE);
        entity.setBalanceAmount(0d);
        entity.setIbanNumber(IbanUtil.generateLuxembourgIban());
        entity.getUsers().add(user);
        BankAccount newAccount = bankAccountRepository.save(entity);

        // create intialisation balance
        this.snapshotBalance(newAccount);

        // attach bank account to user
        user.getBankAccounts().add(newAccount);
        appUserRepository.save(user);

        return newAccount;
    }

    @Override
    public Optional<BankAccount> findByIbanNumber(String ibanNumber) {
        return bankAccountRepository.findByIbanNumber(ibanNumber);


    }

    @Override
    public void updateBankBalance(BankTransaction savedBankTransaction) throws WebBankingApiException {
        BankAccount relatedBankAccount = savedBankTransaction.getBankAccount();

        switch (savedBankTransaction.getType()) {
            case DEPOSIT -> {
            }
            case WITHDRAWAL -> {
            }
            case FEE -> {
            }
            case INTEREST -> {
            }
            case DEBIT -> {
                updateBankBalance(relatedBankAccount, savedBankTransaction.getAmount(), BalanceOperation.SUB);
            }
            case CREDIT -> {
                updateBankBalance(relatedBankAccount, savedBankTransaction.getAmount(), BalanceOperation.ADD);
            }
            default -> {
                throw new WebBankingApiException("Unknown transaction type %s".formatted(savedBankTransaction.getType()));
            }
        }
    }

    @Override
    public Boolean depositMoney(String bankAccountIban, MoneyDepositDto moneyDepositDto) throws WebBankingApiException {
        AppUser user = authAndUserService.getConnectedUser();
        Optional<BankAccount> bankAccount = user.getBankAccounts().stream().filter(ab -> ab.getIbanNumber().equals(bankAccountIban)).findFirst();
        if (bankAccount.isEmpty())
            throw new WebBankingApiException("Account " + bankAccountIban + " is not found in accounts list");
        bankAccount.get().setBalanceAmount(
                currencyConversionService.convertCurrency(moneyDepositDto.getCurrency().toString()
                        , bankAccount.get().getCurrency().toString(),
                        moneyDepositDto.getAmount())
        );
        this.snapshotBalance(bankAccountRepository.save(bankAccount.get()));
        return true;
    }

    @Override
    public Set<BankAccount> getConnectedUserBankAccounts() throws WebBankingApiException {
        AppUser user = authAndUserService.getConnectedUser();
        return user.getBankAccounts();
    }

    private void updateBankBalance(BankAccount relatedBankAccount, Double amount, BalanceOperation balanceOperation) {
        switch (balanceOperation) {
            case ADD -> {
                relatedBankAccount.setBalanceAmount(relatedBankAccount.getBalanceAmount() + amount);
            }
            case SUB -> {
                relatedBankAccount.setBalanceAmount(relatedBankAccount.getBalanceAmount() - amount);
            }
        }
        this.snapshotBalance(relatedBankAccount);
    }

    public void snapshotBalance(BankAccount relatedBankAccount) {
        Balance balance = new Balance();
        balance.setCurrency(relatedBankAccount.getCurrency());
        balance.setBankAccount(relatedBankAccount);
        balance.setType(BalanceType.AVAILABLE);
        balance.setAmount(relatedBankAccount.getBalanceAmount());

        balanceRepository.save(balance);
    }
}
