package com.finologie.banking.api.mappers;

import com.finologie.banking.api.dtos.PaymentCreationDto;
import com.finologie.banking.api.dtos.PaymentMinDto;
import com.finologie.banking.api.entites.Payment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = BankAccountMapper.class)
public interface PaymentMapper {


    Payment toEntity(PaymentCreationDto dto);

    PaymentMinDto toMinDto(Payment entity);


    List<PaymentMinDto> toMinDto(List<Payment> connectedUserPayments);


}
