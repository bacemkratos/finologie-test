package com.finologie.banking.api.entites;

import com.finologie.banking.api.enums.BankAccountStatus;
import com.finologie.banking.api.enums.Currency;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Table(name = "bank_account")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 34, unique = true, updatable = false)
    private String ibanNumber;

    private Double balanceAmount;

    @Enumerated(EnumType.STRING)
    private Currency currency;


    @ManyToMany(mappedBy = "bankAccounts", cascade = CascadeType.MERGE)
    private Set<AppUser> users = new HashSet<>();


    private String AccountName;


    @Enumerated(EnumType.STRING)
    private BankAccountStatus status;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Balance> balances = new HashSet<>();


    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BankTransaction> transactions;


    @OneToMany(mappedBy = "giverAccount", cascade = CascadeType.ALL)
    private Set<Payment> payments;

    //audit attributes

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updateDate;

}
