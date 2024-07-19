package com.finologie.banking.api.services;

import com.finologie.banking.api.dtos.LoginUserDto;
import com.finologie.banking.api.dtos.RegisterUserDto;
import com.finologie.banking.api.dtos.UpdateUserInfoDto;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.exception.WebBankingApiException;

public interface AuthAndUserService {
    AppUser signup(RegisterUserDto input) throws WebBankingApiException;

    AppUser authenticate(LoginUserDto input);

    AppUser getConnectedUser() throws WebBankingApiException;

    AppUser updateConnectedUserInfo(UpdateUserInfoDto registerUserDto) throws WebBankingApiException;

    Boolean logout() throws WebBankingApiException;

    void alertForFraudAttempt() throws WebBankingApiException;

}
