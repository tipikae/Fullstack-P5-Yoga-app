package com.openclassrooms.starterjwt.integration.service;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionServiceITest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private User user;
    private Teacher teacher;
    private Session session;

    @BeforeAll
    void setUp() {
        user = userRepository.save(new User(null, "test@test.com", "user-lastname",
                "user-firstname", "123456", false, LocalDateTime.now(), null));
        teacher = teacherRepository.save(new Teacher(null, "teacher-lastname", "teacher-firstname",
                LocalDateTime.now(), null));
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteById(user.getId());
        teacherRepository.deleteById(teacher.getId());
    }

    @Test
    @Order(1)
    void createReturnsSession() {
        session = sessionService.create(new Session(null, "session-name", new Date(),
                "session-description", teacher, new ArrayList<>(), LocalDateTime.now(), null));
        assertEquals("session-name", session.getName());
    }

    @Test
    @Order(2)
    void getByIdReturnsSessionWhenFound() {
        assertEquals(session.getId(), sessionService.getById(session.getId()).getId());
    }

    @Test
    @Order(3)
    void getByIdReturnsNullWhenNotFound() {
        assertNull(sessionService.getById(10000L));
    }

    @Test
    @Order(4)
    void updateReturnsSession() {
        session.setName(session.getName() + "-update");
        assertEquals(session.getName(), sessionService.update(session.getId(), session).getName());
    }

    @Test
    @Order(5)
    void participateAddUserWhenNotAlreadyParticipated() {
        assertFalse(session.getUsers().contains(user));
        sessionService.participate(session.getId(), user.getId());
        assertTrue(sessionService.getById(session.getId()).getUsers().contains(user));
    }

    @Test
    @Order(6)
    void participateThrowsNotFoundExceptionWhenNotFound() {
        assertThrows(NotFoundException.class, () -> sessionService.participate(session.getId(), 10000L));
        assertThrows(NotFoundException.class, () -> sessionService.participate(10000L, user.getId()));
    }

    @Test
    @Order(7)
    void participateThrowsBadRequestExceptionWhenAlreadyParticipate() {
        assertThrows(BadRequestException.class, () -> sessionService.participate(session.getId(), user.getId()));
    }

    @Test
    @Order(8)
    void noLongerParticipateRemoveUserWhenAlreadyParticipate() {
        assertTrue(sessionService.getById(session.getId()).getUsers().contains(user));
        sessionService.noLongerParticipate(session.getId(), user.getId());
        assertFalse(session.getUsers().contains(user));
    }

    @Test
    @Order(9)
    void noLongerParticipateThrowsNotFoundExceptionWhenNotFound() {
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(10000L, user.getId()));
    }

    @Test
    @Order(10)
    void noLongerParticipateThrowsBadRequestExceptionWhenNotAlreadyParticipated() {
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));
    }

    @Test
    @Order(11)
    void deleteRemoveSession() {
        sessionService.delete(session.getId());
        assertNull(sessionService.getById(session.getId()));
    }
}
