package com.finologie.banking.api.mappers;

import com.finologie.banking.api.dtos.AppUserMinDto;
import com.finologie.banking.api.entites.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AppUserRoleMapper.class)
public interface AppUserMapper {

    AppUserMinDto toMinDto(AppUser entity);

    AppUser toEntity(AppUserMinDto dto);
}
