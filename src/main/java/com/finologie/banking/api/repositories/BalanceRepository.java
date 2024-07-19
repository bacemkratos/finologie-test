package com.finologie.banking.api.repositories;


import com.finologie.banking.api.entites.Balance;
import com.finologie.banking.api.entites.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

}
