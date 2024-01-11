package com.openclassrooms.starterjwt.unit.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SessionMapperITest {

    @MockBean
    private UserService userService;

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private SessionMapper sessionMapper;

    private static User user;
    private static Teacher teacher;
    private static Session session;

    @BeforeAll
    static void setUp() {
        user = new User(1L, "test@test.com", "user-lastname", "user-firstname",
                "123456", false, LocalDateTime.now(), null);
        teacher = new Teacher(1L, "teacher-lastname", "teacher-firstname", LocalDateTime.now(), null);
        session = new Session(1L, "session-name", new Date(), "session-desc", teacher, Arrays.asList(user), LocalDateTime.now(), null);
    }

    @Test
    void toDtoReturnsDto() {
        assertEquals(session.getName(), sessionMapper.toDto(session).getName());
    }

    @Test
    void toEntityReturnsEntity() {
        SessionDto sessionDto = sessionMapper.toDto(session);
        when(userService.findById(anyLong())).thenReturn(user);
        when(teacherService.findById(anyLong())).thenReturn(teacher);
        assertEquals(session.getTeacher().getId(), sessionMapper.toEntity(sessionDto).getId());

        List<SessionDto> sessionDtos = sessionMapper.toDto(Arrays.asList(session));
        assertEquals(1, sessionDtos.size());
        assertEquals(sessionDtos.get(0).getUsers().get(0), session.getUsers().get(0).getId());
    }
}
