package com.openclassrooms.starterjwt.integration.service;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private static User user;

    @BeforeAll
    static void setUp() {
        user = new User(1L, "test@test.com", "lastName", "firstName",
                "123456", false, LocalDateTime.now(), null);
    }

    @Test
    void findByIdShouldReturnUserWhenFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertEquals(user.getEmail(), userService.findById(user.getId()).getEmail());
    }

    @Test
    void findByIdShouldReturnNullWhenNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(userService.findById(user.getId()));
    }

    @Test
    void deleteByIdReturnNothingWhenFoundOrNotFound() {
        doNothing().when(userRepository).deleteById(anyLong());
        userService.delete(user.getId());
        Mockito.verify(userRepository, times(1)).deleteById(user.getId());
    }
}
