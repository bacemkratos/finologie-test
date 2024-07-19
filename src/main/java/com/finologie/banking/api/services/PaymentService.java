package com.finologie.banking.api.services;

import com.finologie.banking.api.dtos.ApiPageRequest;
import com.finologie.banking.api.entites.Payment;
import com.finologie.banking.api.exception.WebBankingApiException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentService {

    Payment createPayment(Payment payment) throws WebBankingApiException;

    void processPayment(Payment payment) throws WebBankingApiException;

    List<Payment> getConnectedUserPayments() throws WebBankingApiException;

    Page<Payment> getConnectedUserPayments(ApiPageRequest pages) throws WebBankingApiException;


    List<Payment> getPaymentsByBeneficiary(String beneficiaryIban, String startDate, String endDate) throws WebBankingApiException;

    List<Payment> getAllNonExecutedPayment();

    Boolean delete(Long paymentId) throws WebBankingApiException;

}
