package com.finologie.banking.api.example_unit_tests;

import com.finologie.banking.api.dtos.LoginUserDto;
import com.finologie.banking.api.dtos.RegisterUserDto;
import com.finologie.banking.api.dtos.UpdateUserInfoDto;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.entites.AppUserRole;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.repositories.AppUserRepository;
import com.finologie.banking.api.repositories.AppUserRoleRepository;
import com.finologie.banking.api.security.BlackListedTokens;
import com.finologie.banking.api.services.impl.AuthAndUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthAndUserServiceImplTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private BlackListedTokens blackListedTokens;

    @Mock
    private AppUserRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthAndUserServiceImpl authAndUserService;

    @BeforeEach
    void setUp() {

        authAndUserService = new AuthAndUserServiceImpl(userRepository,blackListedTokens,roleRepository,authenticationManager,
                passwordEncoder,5);

    }

    @Test
    void signup_userAlreadyExists_throwsException() {
        RegisterUserDto input = new RegisterUserDto();
        input.setUsername("testuser");

        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(new AppUser()));

        WebBankingApiException exception = assertThrows(WebBankingApiException.class, () -> {
            authAndUserService.signup(input);
        });

        assertEquals("User already Exist", exception.getMessage());
    }

    @Test
    void signup_validInput_returnsUser() throws WebBankingApiException {
        RegisterUserDto input = new RegisterUserDto();
        input.setUsername("testuser");
        input.setPassword("password");
        input.setAddress("testaddress");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findByAlias(anyString())).thenReturn(Optional.of(new AppUserRole()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(AppUser.class))).thenReturn(new AppUser());

        AppUser user = authAndUserService.signup(input);

        assertNotNull(user);
        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void authenticate_validInput_returnsUser() {
        LoginUserDto input = new LoginUserDto();
        input.setUsername("testuser");
        input.setPassword("password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new AppUser()));

        AppUser user = authAndUserService.authenticate(input);

        assertNotNull(user);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void getConnectedUser_validUser_returnsUser() throws WebBankingApiException {
        AppUser appUser = new AppUser();
        appUser.setUsername("testuser");

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = new User("testuser", "password", Set.of());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(appUser));

        SecurityContextHolder.setContext(securityContext);

        AppUser user = authAndUserService.getConnectedUser();

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void updateConnectedUserInfo_passwordMismatch_throwsException() {
        UpdateUserInfoDto input = new UpdateUserInfoDto();
        input.setPassword("newpassword");
        input.setConfirmPassword("differentpassword");

        WebBankingApiException exception = assertThrows(WebBankingApiException.class, () -> {
            authAndUserService.updateConnectedUserInfo(input);
        });

        assertEquals("Password miss match", exception.getMessage());
    }

    @Test
    void logout_validToken_returnsTrue() throws WebBankingApiException {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        HashMap<String, Object> details = new HashMap<>();
        details.put("token", "validToken");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(details);

        SecurityContextHolder.setContext(securityContext);

        Boolean result = authAndUserService.logout();

        assertTrue(result);
        verify(blackListedTokens, times(1)).invalidateToken("validToken");
    }


}
