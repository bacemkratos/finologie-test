package com.finologie.banking.api.dtos;

import lombok.Data;

@Data
public class UpdateUserInfoDto {

    private String password;

    private String confirmPassword;

    private String address;
}
