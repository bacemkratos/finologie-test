package com.finologie.banking.api.example_integration_test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finologie.banking.api.dtos.*;
import com.finologie.banking.api.entites.AppUser;
import com.finologie.banking.api.mappers.AppUserMapper;
import com.finologie.banking.api.repositories.AppUserRepository;
import com.finologie.banking.api.security.JWTService;
import com.finologie.banking.api.services.AuthAndUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthAndUserService authAndUserService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void testRegister() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        // Set properties on registerUserDto as necessary
        registerUserDto.setUsername("testuser");
        registerUserDto.setPassword("password");
        registerUserDto.setAddress("testAdress");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"username\":\"testuser\"}"));  // Adjust based on actual response structure
    }

    @Test
    void testAuthenticate() throws Exception {


        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setUsername("joe");
        loginUserDto.setPassword("12345");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isOk());

    }


}
