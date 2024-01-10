package com.openclassrooms.starterjwt.unit.controller;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class SessionControllerTest {

    private static final String ROOT = "/api/session";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    private static Session session;
    private static SessionDto sessionDto;

    @BeforeAll
    static void setUp() {
        User user = new User(1L, "test@test.com", "user-lastname", "user-firstname",
                "123456", false, LocalDateTime.now(), null);
        Teacher teacher = new Teacher(1L, "teacher-lastname", "teacher-firstname",
                LocalDateTime.now(), null);
        session = new Session(1L, "session-name", new Date(), "session-description", teacher,
                Arrays.asList(user), LocalDateTime.now(), null);
        sessionDto = new SessionDto(session.getId(), session.getName(), session.getDate(), session.getTeacher().getId(),
                session.getDescription(), Arrays.asList(session.getUsers().get(0).getId()), session.getCreatedAt(), session.getUpdatedAt());
    }

    @Test
    void findByIdReturns200AndSessionWhenFound() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);
        mockMvc.perform(get(ROOT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(session.getName())));
    }

    @Test
    void findByIdReturns404WhenNotFound() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(null);
        mockMvc.perform(get(ROOT + "/1"))
                .andExpect(status().is(404));
    }

    @Test
    void findByIdReturns400WhenBadIdParameter() throws Exception {
        mockMvc.perform(get(ROOT + "/id"))
                .andExpect(status().is(400));
    }

    @Test
    void findAllReturns200AndList() throws Exception {
        when(sessionService.findAll()).thenReturn(Arrays.asList(session));
        when(sessionMapper.toDto(anyList())).thenReturn(Arrays.asList(sessionDto));
        mockMvc.perform(get(ROOT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is(session.getName())));
    }

    @Test
    void createReturns200AndSession() throws Exception {
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);
        mockMvc.perform(post(ROOT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"id\": 1, " +
                        "\"name\": " +
                        "\"session-name\", " +
                        "\"date\": \"2024-01-10\", " +
                        "\"teacher_id\": 1, " +
                        "\"description\": \"session-description\", " +
                        "\"users\": [1], " +
                        "\"createdAt\": null, " +
                        "\"updatedAt\": null" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(session.getDescription())));
    }

    @Test
    void createReturns400WhenInvalidDto() throws Exception {
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);
        mockMvc.perform(post(ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"id\": \"bad\", " +
                                "\"name\": " +
                                "\"session-name\", " +
                                "\"date\": \"2024-01-10\", " +
                                "\"teacher_id\": 1, " +
                                "\"description\": \"session-description\", " +
                                "\"users\": [1], " +
                                "\"createdAt\": null, " +
                                "\"updatedAt\": null" +
                                "}"))
                .andExpect(status().is(400));
    }

    @Test
    void updateReturns200AndSessionWhenFound() throws Exception {
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);
        mockMvc.perform(put(ROOT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"id\": 1, " +
                                "\"name\": " +
                                "\"session-name\", " +
                                "\"date\": \"2024-01-10\", " +
                                "\"teacher_id\": 1, " +
                                "\"description\": \"session-description\", " +
                                "\"users\": [1], " +
                                "\"createdAt\": null, " +
                                "\"updatedAt\": null" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(session.getDescription())));
    }

    @Test
    void updateReturns400WhenBadIdParameter() throws Exception {
        mockMvc.perform(put(ROOT + "/bad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"id\": 1, " +
                                "\"name\": " +
                                "\"session-name\", " +
                                "\"date\": \"2024-01-10\", " +
                                "\"teacher_id\": 1, " +
                                "\"description\": \"session-description\", " +
                                "\"users\": [1], " +
                                "\"createdAt\": null, " +
                                "\"updatedAt\": null" +
                                "}"))
                .andExpect(status().is(400));
    }

    @Test
    void updateReturns400WhenInvalidDto() throws Exception {
        mockMvc.perform(put(ROOT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"id\": \"bad\", " +
                                "\"name\": " +
                                "\"session-name\", " +
                                "\"date\": \"2024-01-10\", " +
                                "\"teacher_id\": 1, " +
                                "\"description\": \"session-description\", " +
                                "\"users\": [1], " +
                                "\"createdAt\": null, " +
                                "\"updatedAt\": null" +
                                "}"))
                .andExpect(status().is(400));
    }

    @Test
    void deleteReturns200WhenFound() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(session);
        mockMvc.perform(delete(ROOT + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReturns404WhenNotFound() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(null);
        mockMvc.perform(delete(ROOT + "/1"))
                .andExpect(status().is(404));
    }

    @Test
    void deleteReturns400WhenNotFound() throws Exception {
        mockMvc.perform(delete(ROOT + "/bad"))
                .andExpect(status().is(400));
    }

    @Test
    void participateReturns200WhenOK() throws Exception {
        mockMvc.perform(post(ROOT + "/1/participate/1"))
                .andExpect(status().isOk());
    }

    @Test
    void participateReturns400WhenBadPathVariable() throws Exception {
        mockMvc.perform(post(ROOT + "/bad/participate/1"))
                .andExpect(status().is(400));
    }

    @Test
    void noLongerParticipateReturns200WhenOK() throws Exception {
        mockMvc.perform(delete(ROOT + "/1/participate/1"))
                .andExpect(status().isOk());
    }

    @Test
    void noLongerParticipateReturns400WhenBadPathVariable() throws Exception {
        mockMvc.perform(delete(ROOT + "/1/participate/bad"))
                .andExpect(status().is(400));
    }
}
