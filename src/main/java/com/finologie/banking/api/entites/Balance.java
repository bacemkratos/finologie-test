package com.finologie.banking.api.entites;

import com.finologie.banking.api.enums.BalanceType;
import com.finologie.banking.api.enums.Currency;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "balance")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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
    @UpdateTimestamp
    private LocalDateTime updateDate;

}
