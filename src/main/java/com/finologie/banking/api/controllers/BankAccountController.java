package com.finologie.banking.api.controllers;

import com.finologie.banking.api.dtos.BankAccountCreationDto;
import com.finologie.banking.api.dtos.BankAccountMinDto;
import com.finologie.banking.api.dtos.MoneyDepositDto;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.mappers.BankAccountMapper;
import com.finologie.banking.api.services.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Bank Account", description = "Manage Bank Accounts")
@RequestMapping("/api/bank-account")
@RestController
public class BankAccountController {

    private final BankAccountService bankAccountService;

    private final BankAccountMapper bankAccountMapper;

    public BankAccountController(BankAccountService bankAccountService, BankAccountMapper bankAccountMapper) {
        this.bankAccountService = bankAccountService;
        this.bankAccountMapper = bankAccountMapper;
    }

    @PostMapping("")
    @Operation(description = "Open a new bank account")
    public ResponseEntity<BankAccountMinDto> register(@RequestBody BankAccountCreationDto bankAccountCreationDto) throws WebBankingApiException {
        return ResponseEntity.ok(
                this.bankAccountMapper.toMinDto(
                        this.bankAccountService.openAccount(
                                this.bankAccountMapper.toEntity(bankAccountCreationDto))
                )
        );
    }

    @PostMapping("{iban}/deposit")
    @Operation(description = "Deposit money")
    public ResponseEntity<Boolean> depositMoney(@PathVariable(name = "iban") String bankAccountIban, @RequestBody MoneyDepositDto moneyDepositDto) throws WebBankingApiException {
        return ResponseEntity.ok(
                this.bankAccountService.depositMoney(bankAccountIban, moneyDepositDto)
        );
    }

    @GetMapping("")
    @Operation(description = "Get connected user bank accounts")
    public ResponseEntity<Set<BankAccountMinDto>> getUserBankAccounts() throws WebBankingApiException {
        return ResponseEntity.ok(
                this.bankAccountMapper.toMinDto(
                        this.bankAccountService.getConnectedUserBankAccounts()
                )
        );
    }

}