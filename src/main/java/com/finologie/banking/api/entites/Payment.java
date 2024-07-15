package com.finologie.banking.api.entites;

import com.finologie.banking.api.enums.Currency;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "payment")
@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private  BankAccount giverAccount;

    @Column(length = 34 )
    private String beneficiaryAccountNumber ;

    private String beneficiaryName;



    private String communication;


   private boolean excutionStatus ;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private Set<BankTransaction>  transactions;


    //audit attributes

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updateDate;

}
