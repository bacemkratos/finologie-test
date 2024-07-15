package com.finologie.banking.api.entites;

import com.finologie.banking.api.enums.BankAccountStatus;
import com.finologie.banking.api.enums.Currency;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;


@Table(name = "bank_account")
@Entity
@Data
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 34 ,unique = true,updatable = false)
    private String ibanNumber;

    private Double balanceAmount;

    @Enumerated(EnumType.STRING)
    private Currency currency;


    @ManyToMany(mappedBy = "bankAccounts")
    private Set<AppUser> users;


      private  String AccountName;



     @Enumerated(EnumType.STRING)
     private BankAccountStatus status;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Balance> balances;


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
