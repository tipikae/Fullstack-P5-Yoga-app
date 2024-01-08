package com.openclassrooms.starterjwt.integration.service;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    private static User user;
    private static Session session;

    @BeforeAll
    static void setUp() {
        user = new User(1L, "test@test.com", "user-lastName", "user-firstName",
                "123456", false, LocalDateTime.now(), null);
        Teacher teacher = new Teacher(1L, "lastName", "firstName", LocalDateTime.now(), null);
        session = new Session(1L, "session-name", new Date(), "session-desc", teacher,
                new ArrayList<>(), LocalDateTime.now(), null);
    }

    @Test
    void createReturnsSession() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        assertEquals(session.getDescription(), sessionService.create(session).getDescription());
    }

    @Test
    void deleteByIdReturnsNothing() {
        doNothing().when(sessionRepository).deleteById(anyLong());
        sessionService.delete(session.getId());
        Mockito.verify(sessionRepository, times(1)).deleteById(session.getId());
    }

    @Test
    void findAllReturnsSessionList() {
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session));
        assertEquals(1, sessionService.findAll().size());
        assertEquals(session.getName(), sessionService.findAll().get(0).getName());
    }

    @Test
    void getByIdReturnsSessionWhenFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        assertEquals(session.getId(), sessionService.getById(session.getId()).getId());
    }

    @Test
    void getByIdReturnsNullWhenNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(sessionService.getById(session.getId()));
    }

    @Test
    void updateReturnsSession() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        assertEquals(session.getName(), sessionService.update(session.getId(), session).getName());
    }

    @Test
    void participateReturnsNothingWhenOK() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        sessionService.participate(session.getId(), 1000L);
        Mockito.verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void participateThrowsNotFoundExceptionWhenUserNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(session.getId(), 1000L));
    }

    @Test
    void participateThrowsNotFoundExceptionWhenSessionNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertThrows(NotFoundException.class, () -> sessionService.participate(session.getId(), 1000L));
    }

    @Test
    void participateThrowsBadRequestExceptionWhenUserAlreadyParticipated() {
        Session session2 = new Session(session.getId(), session.getName(), session.getDate(), session.getDescription(),
                session.getTeacher(), Arrays.asList(user), session.getCreatedAt(), session.getUpdatedAt());
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session2));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> sessionService.participate(session2.getId(), user.getId()));
    }

    @Test
    void noLongerParticipateReturnsNothingWhenOK() {
        Session session2 = new Session(session.getId(), session.getName(), session.getDate(), session.getDescription(),
                session.getTeacher(), Arrays.asList(user), session.getCreatedAt(), session.getUpdatedAt());
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session2));
        sessionService.noLongerParticipate(session2.getId(), user.getId());
        Mockito.verify(sessionRepository, times(1)).save(session2);
    }

    @Test
    void noLongerParticipateThrowsNotFoundExceptionWhenSessionNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));
    }

    @Test
    void noLongerParticipateThrowsBadRequestExceptionUserNotAlreadyParticipated() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));
    }
}
