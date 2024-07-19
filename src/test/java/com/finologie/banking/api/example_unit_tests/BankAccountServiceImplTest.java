package com.finologie.banking.api.example_unit_tests;

import com.finologie.banking.api.entites.*;
import com.finologie.banking.api.enums.BankAccountStatus;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.repositories.AppUserRepository;
import com.finologie.banking.api.repositories.BalanceRepository;
import com.finologie.banking.api.repositories.BankAccountRepository;
import com.finologie.banking.api.services.AuthAndUserService;
import com.finologie.banking.api.services.CurrencyConversionService;
import com.finologie.banking.api.services.impl.BankAccountServiceImpl;
import com.finologie.banking.api.utils.IbanUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private AuthAndUserService authAndUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private BalanceRepository balanceRepository;

    private BankAccountServiceImpl bankAccountService;

    private AppUser mockUser;

    private BankAccount mockBankAccount;

    @BeforeEach
    void setUp() {

        bankAccountService = new BankAccountServiceImpl(bankAccountRepository,authAndUserService,
                currencyConversionService,appUserRepository,balanceRepository);

        mockUser = new AppUser();
        mockUser.setUsername("testuser");
        mockUser.setBankAccounts(Set.of());

        mockBankAccount = new BankAccount();
        mockBankAccount.setIbanNumber(IbanUtil.generateLuxembourgIban());
        mockBankAccount.setStatus(BankAccountStatus.ACTIVE);
        mockBankAccount.setBalanceAmount(0d);
        mockBankAccount.setUsers(Set.of(mockUser));
    }



    @Test
    void findByIbanNumber_existingIban_returnsBankAccount() {
        when(bankAccountRepository.findByIbanNumber(anyString())).thenReturn(Optional.of(mockBankAccount));

        Optional<BankAccount> foundAccount = bankAccountService.findByIbanNumber(mockBankAccount.getIbanNumber());

        assertTrue(foundAccount.isPresent());
        assertEquals(mockBankAccount.getIbanNumber(), foundAccount.get().getIbanNumber());
    }



    @Test
    void getConnectedUserBankAccounts_validUser_returnsAccounts() throws WebBankingApiException {
        when(authAndUserService.getConnectedUser()).thenReturn(mockUser);

        Set<BankAccount> bankAccounts = bankAccountService.getConnectedUserBankAccounts();

        assertNotNull(bankAccounts);
        assertEquals(0, bankAccounts.size());
    }


}
