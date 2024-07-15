package com.finologie.banking.api.controllers;

import com.finologie.banking.api.dtos.AppUserMinDto;
import com.finologie.banking.api.dtos.LoginResponse;
import com.finologie.banking.api.dtos.LoginUserDto;
import com.finologie.banking.api.dtos.RegisterUserDto;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.mappers.AppUserMapper;
import com.finologie.banking.api.security.JWTService;
import com.finologie.banking.api.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthController {
    private final JWTService jwtService;

    private final AuthenticationService authenticationService;

    private final AppUserMapper appUserMapper;

    public AuthController(JWTService jwtService, AuthenticationService authenticationService, AppUserMapper appUserMapper) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.appUserMapper = appUserMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<AppUserMinDto> register(@RequestBody RegisterUserDto registerUserDto) throws WebBankingApiException {
        AppUser registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(appUserMapper.toMinDto(registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        AppUser authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}