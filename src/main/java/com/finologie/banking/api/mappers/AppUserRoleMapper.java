package com.finologie.banking.api.mappers;

import com.finologie.banking.api.dtos.AppUserRoleDto;
import com.finologie.banking.api.entites.AppUserRole;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface AppUserRoleMapper {


    AppUserRoleDto toDto(AppUserRole entity);

    AppUserRole toEntity(AppUserRoleDto dto);

    Set<AppUserRole> toEntitySet(Set<AppUserRole> employees);

    Set<AppUserRoleDto> toDtoSet(Set<AppUserRole> employees);
}
