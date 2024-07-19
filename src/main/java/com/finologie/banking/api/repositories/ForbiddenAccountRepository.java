package com.finologie.banking.api.repositories;


import com.finologie.banking.api.entites.ForbiddenAccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForbiddenAccountRepository extends JpaRepository<ForbiddenAccountNumber, String> {

}
