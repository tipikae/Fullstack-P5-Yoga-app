package com.openclassrooms.starterjwt.unit.controller;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    private static final String ROOT = "/api/auth";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private UserRepository userRepository;

    private static User user;

    @BeforeAll
    static void setUp() {
        user = new User(1L, "test@test.com", "user-lastname", "user-firstname",
                "123456", false, LocalDateTime.now(), null);
    }

    @Test
    void authenticateUserReturns200AndJwtResponseWhenOk() throws Exception {
        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isAdmin(), user.getPassword());
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("jwt");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        mockMvc.perform(post(ROOT + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@test.com\", \"password\": \"123456\"}"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.token", is("jwt")));
    }

    @Test
    void authenticateUserReturns401WhenBadCredentials() throws Exception {
        when(authenticationManager.authenticate(any())).thenThrow(new MyAthenticationException("Not authorized"));
        mockMvc.perform(post(ROOT + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@test.com\", \"password\": \"123456\"}"))
                .andExpect(status().is(401));
    }

    @Test
    void registerReturns200AndMessageWhenUserNotAlreadyExists() throws Exception {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        mockMvc.perform(post(ROOT + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"test@test.com\", " +
                        "\"firstName\": \"user-firstname\", " +
                        "\"lastName\": \"user-lastname\", " +
                        "\"password\": \"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User registered successfully!")));
    }

    @Test
    void registerReturns400AndMessageWhenUserAlreadyExists() throws Exception {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        mockMvc.perform(post(ROOT + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\": \"test@test.com\", " +
                                "\"firstName\": \"user-firstname\", " +
                                "\"lastName\": \"user-lastname\", " +
                                "\"password\": \"123456\"}"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Error: Email is already taken!")));
    }
}

class MyAthenticationException extends AuthenticationException {
    public MyAthenticationException(String msg) {
        super(msg);
    }
}
