package com.finologie.banking.api.services.impl;

import com.finologie.banking.api.dtos.ApiPageRequest;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.entites.BankAccount;
import com.finologie.banking.api.entites.BankTransaction;
import com.finologie.banking.api.entites.Payment;
import com.finologie.banking.api.enums.TransactionType;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.exception.WebBankingApiFraudException;
import com.finologie.banking.api.repositories.BankAccountRepository;
import com.finologie.banking.api.repositories.PaymentRepository;
import com.finologie.banking.api.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final IbanValidationService ibanValidationService;

    private final CurrencyConversionService currencyConversionService;

    private final PaymentRepository paymentRepository;

    private final BankAccountService bankAccountService;

    private final BankTransactionService bankTransactionService;

    private final AuthAndUserService authAndUserService;

    private final String dateFormat;


    public PaymentServiceImpl(IbanValidationService ibanValidationService, CurrencyConversionService currencyConversionService,
                              PaymentRepository paymentRepository, BankAccountRepository bankAccountRepository,
                              BankAccountService bankAccountService, BankTransactionService bankTransactionService,
                              AuthAndUserService authAndUserService, @Value("${app.config.dateformat}") String dateFormat) {
        this.ibanValidationService = ibanValidationService;
        this.currencyConversionService = currencyConversionService;
        this.paymentRepository = paymentRepository;
        this.bankAccountService = bankAccountService;
        this.bankTransactionService = bankTransactionService;

        this.authAndUserService = authAndUserService;
        this.dateFormat = dateFormat;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Payment createPayment(Payment payment) throws WebBankingApiException {
        AppUser connectedUser = authAndUserService.getConnectedUser();
        try {
            validatePayment(payment, connectedUser);
        }catch (WebBankingApiFraudException e){
            authAndUserService.alertForFraudAttempt();
            throw  e;
        }

        //everything is fine, lets create our payment

        BankAccount giverAccount = bankAccountService.findByIbanNumber(payment.getGiverAccount().getIbanNumber()).get();


        // save tha payment first
        payment.setGiverAccount(giverAccount);
        payment.setExcutionStatus(false);
        payment = paymentRepository.save(payment);
        return payment;

    }

    @Override
    @Transactional
    public void processPayment(Payment payment) throws WebBankingApiException {
         payment = paymentRepository.findById(payment.getId()).get();
        Optional<BankAccount> beneficiaryAccount = bankAccountService.findByIbanNumber(payment.getBeneficiaryAccountNumber());
        //create giver transaction
        BankTransaction giverTransaction = bankTransactionService.createInnerPaymentTransaction(payment, payment.getGiverAccount(), TransactionType.DEBIT);
        bankTransactionService.processTransaction(giverTransaction);
        //create Beneficiary transaction
        BankTransaction beneficiaryTransaction;
        if (beneficiaryAccount.isPresent()) {
            beneficiaryTransaction = bankTransactionService.createInnerPaymentTransaction(payment, beneficiaryAccount.get(), TransactionType.CREDIT);
            bankTransactionService.processTransaction(beneficiaryTransaction);
        } else {
            beneficiaryTransaction = bankTransactionService.createExternalPaymentTransaction(payment, payment.getBeneficiaryAccountNumber(), TransactionType.CREDIT);
        }
        payment.getTransactions().addAll(Set.of(giverTransaction, beneficiaryTransaction));
        payment.setExcutionStatus(true);
        paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getConnectedUserPayments() throws WebBankingApiException {
        AppUser user = this.authAndUserService.getConnectedUser();
        Set<String> ibans = user.getBankAccounts().stream().map(BankAccount::getIbanNumber).collect(Collectors.toSet());

            return   paymentRepository.finPaymentsByIbanList(ibans);



    }

    @Override
    public Page<Payment> getConnectedUserPayments(ApiPageRequest pages) throws WebBankingApiException {
        AppUser user = this.authAndUserService.getConnectedUser();
        Set<String> ibans = user.getBankAccounts().stream().map(BankAccount::getIbanNumber).collect(Collectors.toSet());



        return   paymentRepository.finPaymentsByIbanList(ibans, PageRequest.of(pages.getPageNumber(), pages.getSize()));
    }

    @Override
    public List<Payment> getPaymentsByBeneficiary(String beneficiaryIban, String startDateString, String endDateString) throws WebBankingApiException {
        AppUser user = this.authAndUserService.getConnectedUser();
        Set<String> ibans = user.getBankAccounts().stream().map(BankAccount::getIbanNumber).collect(Collectors.toSet());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);


        return paymentRepository.findByBenifciaryIbanBetweenDates(ibans, beneficiaryIban, startDate.atStartOfDay(), endDate.atStartOfDay());
    }

    @Override
    public List<Payment> getAllNonExecutedPayment() {
        return  paymentRepository.findAllByExcutionStatusFalse();
    }

    @Override
    public Boolean delete(Long paymentId) throws WebBankingApiException {
        AppUser user = authAndUserService.getConnectedUser();
        Optional<Payment> payment = this.paymentRepository.findByIdAndUserId(paymentId,user.getId());
        if(payment.isEmpty()) throw  new  WebBankingApiException(" payment not found");
        if(payment.get().isExcutionStatus()) throw  new  WebBankingApiException(" payment already executed, cannot delete payment");

        paymentRepository.delete(payment.get());

        return true;
    }


    private void validatePayment(Payment payment, AppUser connectedUser) throws WebBankingApiException {
        // check if the giver account exist in list of connected user accounts
        Optional<BankAccount> giverAccount = connectedUser.getBankAccounts().stream()
                .filter(b -> b.getIbanNumber().equalsIgnoreCase(payment.getGiverAccount().getIbanNumber())).
                findFirst();
        if (giverAccount.isEmpty())
            throw new WebBankingApiException("account %s is not found in accounts list".formatted(payment.getGiverAccount().getIbanNumber()));

        // check auto payment
        if (giverAccount.get().getIbanNumber().equalsIgnoreCase(payment.getBeneficiaryAccountNumber()))
            throw new WebBankingApiException("Beneficiary account should be not same as giver");

        // check if payment amount exceed current balance
        double paymentAmount = currencyConversionService.convertCurrency(payment.getCurrency().toString(),
                giverAccount.get().getCurrency().toString(),
                payment.getAmount());

        if ((giverAccount.get().getBalanceAmount() - paymentAmount) < 0)
            throw new WebBankingApiException("insufficient funds to proceed the payment");

        //check of beneficiary iban is valid but first need to check that the iban is internal or external
        Optional<BankAccount> account = bankAccountService.findByIbanNumber(payment.getBeneficiaryAccountNumber());

        if (!ibanValidationService.validateIban(payment.getBeneficiaryAccountNumber(), account.isPresent()))
            throw new WebBankingApiException("Iban %s is not valid".formatted(payment.getBeneficiaryAccountNumber()));
    }
}
