package com.finologie.banking.api.entites;

import com.finologie.banking.api.enums.BalanceType;
import com.finologie.banking.api.enums.Currency;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "balance")
@Entity
@Data
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime date;


    @Enumerated(EnumType.STRING)
    private BalanceType type;


    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    //audit attributes

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updateDate;

}
