package com.openclassrooms.starterjwt.unit.security;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserIsFound() {
        User user = new User(1L, "test@test.com", "user-lastname", "user-firstname",
                "123456", false, LocalDateTime.now(), null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        assertEquals(user.getPassword(), userDetailsService.loadUserByUsername("test@test.com").getPassword());
    }

    @Test
    void loadUserByUsernameReturnsUsernameNotFoundExceptionWhenUserIsNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("test@test.com"));
    }
}
