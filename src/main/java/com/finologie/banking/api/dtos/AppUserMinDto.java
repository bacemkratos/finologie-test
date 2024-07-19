package com.finologie.banking.api.dtos;

import lombok.Data;

import java.util.Set;


@Data
public class AppUserMinDto {

    private String username;

    private String address;


    private Set<AppUserRoleDto> roles;


    private Boolean isAccountNonExpired;


    private Boolean isAccountNonLocked;


    private Boolean isEnabled;


}
