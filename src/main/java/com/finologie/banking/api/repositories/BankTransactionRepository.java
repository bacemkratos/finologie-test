package com.finologie.banking.api.repositories;


import com.finologie.banking.api.entites.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {


}
