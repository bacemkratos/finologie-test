package com.finologie.banking.api.dtos;

import com.finologie.banking.api.entites.AppUserRole;
import com.finologie.banking.api.entites.BankAccount;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;


@Data
public class AppUserMinDto {

    private String username;

    private String address;


    private Set<AppUserRoleDto> roles;


    private Boolean isAccountNonExpired;


    private Boolean isAccountNonLocked;


    private Boolean isEnabled;




}
