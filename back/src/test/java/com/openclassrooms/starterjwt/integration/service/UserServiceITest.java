package com.openclassrooms.starterjwt.integration.service;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceITest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeAll
    void setUp() {
        user = userRepository.save(new User(null, "test@test.com", "user-lastname",
                "user-firstname", "123456", false, LocalDateTime.now(), null));
    }

    @Test
    @Order(1)
    void findByIdReturnsUserWhenFound() {
        assertEquals(user.getEmail(), userService.findById(user.getId()).getEmail());
    }

    @Test
    @Order(2)
    void findByIdReturnsNullWhenNotFound() {
        assertNull(userService.findById(10000L));
    }

    @Test
    @Order(3)
    void deleteReturnsNothingAndDeleteUserWhenFound() {
        assertTrue(userRepository.existsById(user.getId()));
        userService.delete(user.getId());
        assertFalse(userRepository.existsById(user.getId()));
    }
}
