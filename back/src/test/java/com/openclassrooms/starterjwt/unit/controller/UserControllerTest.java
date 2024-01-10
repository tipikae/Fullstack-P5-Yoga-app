package com.openclassrooms.starterjwt.unit.controller;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@test.com")
public class UserControllerTest {

    private static final String ROOT = "/api/user";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private static User user;

    @BeforeAll
    static void setUp() {
        user = new User(1L, "test@test.com", "user-lastname", "user-firstname",
                "123456", false, LocalDateTime.now(), null);
    }

    @Test
    void findByIdReturns200AndUserWhenUserIsFound() throws Exception {
        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getLastName(), user.getFirstName(), user.isAdmin(),
                user.getPassword(), user.getCreatedAt(), user.getUpdatedAt());
        when(userService.findById(anyLong())).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);
        mockMvc.perform(get(ROOT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void findByIdReturns404WhenUserIsNotFound() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);
        mockMvc.perform(get(ROOT + "/1"))
                .andExpect(status().is(404));
    }

    @Test
    void findByIdReturns400WhenBadIdParameter() throws Exception {
        mockMvc.perform(get(ROOT + "/test"))
                .andExpect(status().is(400));
    }

    @Test
    void deleteReturns200WhenUserIsFound() throws Exception {
        when(userService.findById(anyLong())).thenReturn(user);
        mockMvc.perform(delete(ROOT + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReturns404WhenUserIsNotFound() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);
        mockMvc.perform(delete(ROOT + "/1"))
                .andExpect(status().is(404));
    }

    @Test
    void deleteReturns400WhenBadIdParameter() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);
        mockMvc.perform(delete(ROOT + "/test"))
                .andExpect(status().is(400));
    }

    @Test
    void deleteReturns401WhenBadCredentials() throws Exception {
        User badUser = new User(user.getId(), "bad@test.com", user.getLastName(), user.getFirstName(),
                user.getPassword(), user.isAdmin(), user.getUpdatedAt(), user.getUpdatedAt());
        when(userService.findById(anyLong())).thenReturn(badUser);
        mockMvc.perform(delete(ROOT + "/1"))
                .andExpect(status().is(401));
    }
}
