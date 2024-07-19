package com.finologie.banking.api.mappers;

import com.finologie.banking.api.dtos.BankAccountCreationDto;
import com.finologie.banking.api.dtos.BankAccountIbanOnlyDto;
import com.finologie.banking.api.dtos.BankAccountMinDto;
import com.finologie.banking.api.entites.BankAccount;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    BankAccountMinDto toMinDto(BankAccount entity);

    Set<BankAccountMinDto> toMinDto(Set<BankAccount> entity);

    BankAccount toEntity(BankAccountMinDto dto);

    BankAccount toEntity(BankAccountIbanOnlyDto dto);


    BankAccount toEntity(BankAccountCreationDto dto);
}
