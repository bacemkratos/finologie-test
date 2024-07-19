package com.finologie.banking.api.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "forbidden_account")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ForbiddenAccountNumber {
    @Id
    @Column(length = 34, unique = true, updatable = false)
    @EqualsAndHashCode.Include
    private String iban;


}
