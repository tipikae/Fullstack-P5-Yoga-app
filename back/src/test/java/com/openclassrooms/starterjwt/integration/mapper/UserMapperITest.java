package com.openclassrooms.starterjwt.integration.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserMapperITest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void test() {
        User user = new User(1L, "test@test.com", "teacher-lastname", "teacher-firstname",
                "123456", false, LocalDateTime.now(), null);
        // toDto
        UserDto userDto = userMapper.toDto(user);
        assertEquals(user.getId(), userDto.getId());
        // toEntity
        assertEquals(userDto.getLastName(), userMapper.toEntity(userDto).getLastName());

        List<User> users = Arrays.asList(user);
        // toDto
        List<UserDto> userDtos = userMapper.toDto(users);
        assertTrue(userDtos.size() == 1);
        assertEquals(users.get(0).getFirstName(), userDtos.get(0).getFirstName());
        // toEntity
        assertTrue(userMapper.toEntity(userDtos).size() == 1);
        assertEquals(userDtos.get(0).getCreatedAt(), userMapper.toEntity(userDtos).get(0).getCreatedAt());
    }
}
