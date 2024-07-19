package com.finologie.banking.api.repositories;


import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.entites.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByIbanNumber(String beneficiaryAccountNumber);
}
