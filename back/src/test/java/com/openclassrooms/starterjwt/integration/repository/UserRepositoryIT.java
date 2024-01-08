package com.openclassrooms.starterjwt.integration.repository;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void test() {
        User user = new User();
        user.setAdmin(false);
        user.setEmail("test-IT@test.com");
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword("123456");
        user.setLastName("user-lastname");
        user.setFirstName("user-firstname");

        // save
        User userSaved = userRepository.save(user);
        assertNotNull(userSaved);
        assertEquals(user.getFirstName(), userSaved.getFirstName());

        // get one by id
        assertEquals(user.getLastName(), userRepository.findById(userSaved.getId()).get().getLastName());

        // get one by email
        assertEquals(userSaved.getId(), userRepository.findByEmail(userSaved.getEmail()).get().getId());

        // exists by email
        assertTrue(userRepository.existsByEmail(user.getEmail()));

        // delete
        userRepository.deleteById(userSaved.getId());
        assertFalse(userRepository.existsById(userSaved.getId()));
    }
}
