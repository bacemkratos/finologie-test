package com.finologie.banking.api.controllers;


import com.finologie.banking.api.dtos.AppUserMinDto;
import com.finologie.banking.api.dtos.UpdateUserInfoDto;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.mappers.AppUserMapper;
import com.finologie.banking.api.services.AuthAndUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name ="Profile",description = "Manage profile")
@RequestMapping("/api/profile")
@RestController
public class ProfileController {


    private final AuthAndUserService authAndUserService;

    private final AppUserMapper appUserMapper;

    public ProfileController(AuthAndUserService authAndUserService, AppUserMapper appUserMapper) {
        this.authAndUserService = authAndUserService;
        this.appUserMapper = appUserMapper;
    }

    @PostMapping("/update")
    public ResponseEntity<AppUserMinDto> register(@RequestBody UpdateUserInfoDto registerUserDto) throws WebBankingApiException {
        AppUser registeredUser = authAndUserService.updateConnectedUserInfo(registerUserDto);

        return ResponseEntity.ok(appUserMapper.toMinDto(registeredUser));
    }

}
