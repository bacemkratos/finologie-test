package com.finologie.banking.api.services.impl;

import com.finologie.banking.api.dtos.LoginUserDto;
import com.finologie.banking.api.dtos.RegisterUserDto;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.entites.AppUserRole;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.repositories.AppUserRepository;
import com.finologie.banking.api.repositories.AppUserRoleRepository;
import com.finologie.banking.api.services.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AppUserRepository userRepository;

    private final AppUserRoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(
            AppUserRepository userRepository, AppUserRoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser signup(RegisterUserDto input) throws WebBankingApiException {
        Optional<AppUser> searchResult = userRepository.findByUsername(input.getUsername());
        if(searchResult.isPresent())
            throw  new WebBankingApiException("User already Exist");

        AppUser user = new AppUser();
        user.setUsername(input.getUsername());
        user.setAddress(input.getAddress());
        Optional<AppUserRole> role = roleRepository.findByAlias("USER");
        if (role.isEmpty()){
            throw  new WebBankingApiException("Role Does not exist in database");
        }
         user.setRoles(Set.of(role.get()));
        user.setIsEnabled(true);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public AppUser authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}
