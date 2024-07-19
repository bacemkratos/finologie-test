package com.finologie.banking.api.repositories;


import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.entites.ForbiddenAccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForbiddenAccountRepository extends JpaRepository<ForbiddenAccountNumber, String> {

}
