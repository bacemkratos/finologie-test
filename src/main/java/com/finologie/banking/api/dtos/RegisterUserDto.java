package com.finologie.banking.api.dtos;

import lombok.Data;

@Data
public class RegisterUserDto {

    private String username;

    private String password;

    private String address;
}