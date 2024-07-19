package com.finologie.banking.api.repositories;


import com.finologie.banking.api.entites.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select  p from Payment p join p.giverAccount ba where ba.ibanNumber in ?1  order by p.creationDate desc ")
    List<Payment> finPaymentsByIbanList(Set<String> id);


    @Query("select  p from Payment p  join p.giverAccount ba  where ba.ibanNumber in :ibans  and p.beneficiaryAccountNumber = :beneficiaryIban and  p.creationDate >= :startDate  and    p.creationDate <= :endDate order by p.creationDate desc ")
    List<Payment> findByBenifciaryIbanBetweenDates(@Param("ibans") Set<String> ibans, @Param("beneficiaryIban") String beneficiaryIban, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Payment> findAllByExcutionStatusFalse();

    @Query("select  p from Payment p join p.giverAccount ba left join ba.users u  where p.id = :id  and u.id = :userid  order by p.creationDate desc ")
    Optional<Payment> findByIdAndUserId(@Param("id") Long paymentId, @Param("userid") Long id);

    @Query("select  p from Payment p join p.giverAccount ba where ba.ibanNumber in ?1  order by p.creationDate desc ")
    Page<Payment> finPaymentsByIbanList(Set<String> ibans, Pageable pages);

}
