package com.finologie.banking.api.services;

import com.finologie.banking.api.dtos.LoginUserDto;
import com.finologie.banking.api.dtos.RegisterUserDto;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.exception.WebBankingApiException;

public interface AuthenticationService {
    AppUser signup(RegisterUserDto input) throws WebBankingApiException;

    AppUser authenticate(LoginUserDto input);
}
