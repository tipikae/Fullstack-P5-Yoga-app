package com.openclassrooms.starterjwt.integration.security;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilsITest {

    @Autowired
    private JwtUtils jwtUtils;

    private static Authentication authentication;

    @BeforeAll
    static void setUp() {
        authentication = new Authentication() {
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
                return new UserDetailsImpl(1L, "test@test.com", "user-firstname", "user-lastname", false, "123456");
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
    }

    @Test
    void test() {
        // generateJwtToken
        String token = jwtUtils.generateJwtToken(authentication);
        assertFalse(token.isEmpty());
        // getUserNameFromJwtToken
        assertEquals("test@test.com", jwtUtils.getUserNameFromJwtToken(token));
        // validateJwtToken
        assertTrue(jwtUtils.validateJwtToken(token));
        assertFalse(jwtUtils.validateJwtToken(token.substring(0, token.length() - 1)));
    }
}
