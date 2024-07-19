package com.finologie.banking.api.controllers;

import com.finologie.banking.api.dtos.AppUserMinDto;
import com.finologie.banking.api.dtos.LoginResponse;
import com.finologie.banking.api.dtos.LoginUserDto;
import com.finologie.banking.api.dtos.RegisterUserDto;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.exception.WebBankingApiFraudException;
import com.finologie.banking.api.mappers.AppUserMapper;
import com.finologie.banking.api.security.JWTService;
import com.finologie.banking.api.services.AuthAndUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Authentication apis")
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final JWTService jwtService;

    private final AuthAndUserService authAndUserService;

    private final AppUserMapper appUserMapper;

    public AuthController(JWTService jwtService, AuthAndUserService authAndUserService, AppUserMapper appUserMapper) {
        this.jwtService = jwtService;
        this.authAndUserService = authAndUserService;
        this.appUserMapper = appUserMapper;
    }

    @Operation(description = "Create  account")
    @PostMapping("/signup")
    public ResponseEntity<AppUserMinDto> register(@RequestBody RegisterUserDto registerUserDto) throws WebBankingApiException {
        AppUser registeredUser = authAndUserService.signup(registerUserDto);

        return ResponseEntity.ok(appUserMapper.toMinDto(registeredUser));
    }

    @Operation(description = "Log in")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws WebBankingApiException {
        AppUser authenticatedUser = authAndUserService.authenticate(loginUserDto);
        if (!authenticatedUser.isAccountNonLocked()) {
            authAndUserService.logout();
            throw new WebBankingApiFraudException("Account is locked, please contact your bank consultant");
        }
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @Operation(description = "Log out")
    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout() throws WebBankingApiException {


        return ResponseEntity.ok(authAndUserService.logout());
    }


}