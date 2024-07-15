package com.finologie.banking.api.repositories;


import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.entites.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Long> {
    Optional<AppUserRole> findByAlias(String alias);
}
