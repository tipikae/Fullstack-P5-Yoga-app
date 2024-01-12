package com.openclassrooms.starterjwt.unit.security;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    @Test
    void test() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(1L, "test@test.com", "user-firstname",
                "user-lastname", false, "123456");
        assertTrue(userDetails1.getAuthorities().isEmpty());
        assertTrue(userDetails1.isAccountNonExpired());
        assertTrue(userDetails1.isAccountNonLocked());
        assertTrue(userDetails1.isCredentialsNonExpired());
        assertTrue(userDetails1.isEnabled());
        assertTrue(userDetails1.equals(userDetails1));

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("user-firstname")
                .lastName("user-lastname")
                .admin(false)
                .password("123456")
                .build();
        assertTrue(userDetails1.equals(userDetails2));

        UserDetailsImpl userDetails3 = new UserDetailsImpl(2L, "test@test.com", "user-firstname",
                "user-lastname", false, "123456");
        assertFalse(userDetails1.equals(userDetails3));

        assertFalse(userDetails1.equals(null));
    }
}
