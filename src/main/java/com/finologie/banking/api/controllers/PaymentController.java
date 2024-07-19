package com.finologie.banking.api.controllers;

import com.finologie.banking.api.dtos.ApiPageRequest;
import com.finologie.banking.api.dtos.PaymentCreationDto;
import com.finologie.banking.api.dtos.PaymentMinDto;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.mappers.PaymentMapper;
import com.finologie.banking.api.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payments", description = "Manage payments")
@RequestMapping("/api/payment")
@RestController
public class PaymentController {

    private final PaymentMapper paymentMapper;
    private final PaymentService paymentService;

    public PaymentController(PaymentMapper paymentMapper, PaymentService paymentService) {
        this.paymentMapper = paymentMapper;
        this.paymentService = paymentService;
    }


    @PostMapping("")
    @Operation(summary = "Create Payment")
    public ResponseEntity<PaymentMinDto> register(@RequestBody PaymentCreationDto paymentCreationDto) throws WebBankingApiException {
        return ResponseEntity.ok(
                this.paymentMapper.toMinDto(
                        this.paymentService.createPayment(
                                this.paymentMapper.toEntity(paymentCreationDto))
                )
        );
    }

    @GetMapping("")
    @Operation(summary = "List of connected user Payments")
    public ResponseEntity<List<PaymentMinDto>> getAllPayment() throws WebBankingApiException {
        return ResponseEntity.ok(
                this.paymentMapper.toMinDto(
                        this.paymentService.getConnectedUserPayments()
                )
        );
    }

    @PostMapping("get-all")
    @Operation(summary = "List of connected user Payments paged ")
    public ResponseEntity<List<PaymentMinDto>> PagedPayment(@RequestBody ApiPageRequest pages) throws WebBankingApiException {
        // for some reason swagger does not detect that he payload is not required, to exploit the full potential api , try it with postman
        return ResponseEntity.ok(
                this.paymentMapper.toMinDto(
                        this.paymentService.getConnectedUserPayments(pages).getContent()
                )
        );
    }


    @GetMapping("/beneficiary/{iban}")
    @Operation(summary = "List of  Payments by beneficiary iban ")
    public ResponseEntity<List<PaymentMinDto>> getAllPaymentsByBeneficiary(@PathVariable(name = "iban") String beneficiaryIban,
                                                                           @RequestParam("startDate") String startDate,
                                                                           @RequestParam("enDate") String endDate
    ) throws WebBankingApiException {
        return ResponseEntity.ok(
                this.paymentMapper.toMinDto(
                        this.paymentService.getPaymentsByBeneficiary(beneficiaryIban, startDate, endDate)
                )
        );
    }


    @DeleteMapping("/{paymentId}")
    @Operation(summary = "Delete non executed Payments")
    public ResponseEntity<Boolean> deletePayment(@PathVariable(name = "paymentId") Long paymentId) throws WebBankingApiException {
        return ResponseEntity.ok(
                this.paymentService.delete(paymentId)
        );
    }
}