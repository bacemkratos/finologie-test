package com.finologie.banking.api.services.impl;

import com.finologie.banking.api.dtos.LoginUserDto;
import com.finologie.banking.api.dtos.RegisterUserDto;
import com.finologie.banking.api.dtos.UpdateUserInfoDto;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.entites.AppUserRole;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.repositories.AppUserRepository;
import com.finologie.banking.api.repositories.AppUserRoleRepository;
import com.finologie.banking.api.security.BlackListedTokens;
import com.finologie.banking.api.services.AuthAndUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthAndUserServiceImpl implements AuthAndUserService {

    public static final String USER_ROLE = "USER";
    public static final String TOKEN = "token";
    private final AppUserRepository userRepository;

    private final BlackListedTokens blackListedTokens;

    private final AppUserRoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final int maxFraudTentative;


    public AuthAndUserServiceImpl(
            AppUserRepository userRepository, BlackListedTokens blackListedTokens, AppUserRoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            @Value("${app.config.max-fraud-tentative}") int maxFraudTentative
    ) {
        this.blackListedTokens = blackListedTokens;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.maxFraudTentative = maxFraudTentative;
    }

    @Override
    public AppUser signup(RegisterUserDto input) throws WebBankingApiException {
        //check if user name already exists
        Optional<AppUser> searchResult = userRepository.findByUsername(input.getUsername());
        if (searchResult.isPresent())
            throw new WebBankingApiException("User already Exist");

        //init  and create user
        AppUser user = new AppUser();
        user.setUsername(input.getUsername());
        user.setAddress(input.getAddress());
        user.setForbiddenActionsCount(0);
        Optional<AppUserRole> role = roleRepository.findByAlias(USER_ROLE);
        // role USER is default user it should exist
        if (role.isEmpty()) {
            throw new WebBankingApiException("Role Does not exist in database");
        }
        user.setRoles(Set.of(role.get()));
        user.setIsEnabled(true);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        //encode the password
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

    @Override
    public AppUser getConnectedUser() throws WebBankingApiException {
        // normally we should be careful with null pointer when authentication context is null
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<AppUser> search = userRepository.findByUsername(user.getUsername());
        if (search.isPresent())
            return search.get();
        else
            throw new WebBankingApiException("Cannot find connected user");
    }

    @Override
    public AppUser updateConnectedUserInfo(UpdateUserInfoDto updateUserInfoDto) throws WebBankingApiException {
        if (!updateUserInfoDto.getConfirmPassword().equals(updateUserInfoDto.getPassword()))
            throw new WebBankingApiException("Password miss match");
        AppUser user = getConnectedUser();
        if (StringUtils.isNotBlank(updateUserInfoDto.getAddress()))
            user.setAddress(updateUserInfoDto.getAddress());
        if (StringUtils.isNotBlank(updateUserInfoDto.getPassword())) {
            user.setPassword(passwordEncoder.encode(updateUserInfoDto.getPassword()));
            logout();
        }

        return userRepository.save(user);
    }

    @Override
    public Boolean logout() throws WebBankingApiException {
        if (SecurityContextHolder.getContext() == null) throw new WebBankingApiException("user not connected ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        HashMap<String, Object> details = (HashMap<String, Object>) auth.getDetails();
        if (!details.containsKey(TOKEN)) throw new WebBankingApiException("token does not exist");
        blackListedTokens.invalidateToken((String) details.get(TOKEN));

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void alertForFraudAttempt() throws WebBankingApiException {
        AppUser user = getConnectedUser();
        user.setForbiddenActionsCount(user.getForbiddenActionsCount() + 1);
        if (user.getForbiddenActionsCount() > maxFraudTentative) {
            user.setIsAccountNonLocked(false);
            logout();
        }
        userRepository.save(user);
    }


}
