package com.finologie.banking.api.jobs;

import com.finologie.banking.api.entites.Payment;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.services.PaymentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class PaymentsJob {

    private final PaymentService paymentService;


    public PaymentsJob(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @Scheduled(fixedRate = 600000, initialDelay = 20000)
    public void scheduleFixedRateTask() throws WebBankingApiException {
        List<Payment> nonExecutedPayments = paymentService.getAllNonExecutedPayment();
        if (nonExecutedPayments.size() > 0) {
            log.info("Processing %d non executed  payments ".formatted(nonExecutedPayments.size()));
            for (Payment nonExecutedPayment : nonExecutedPayments) {
                paymentService.processPayment(nonExecutedPayment);
            }
            log.info("Processing   payments ended successfully ");
        }

    }
}
