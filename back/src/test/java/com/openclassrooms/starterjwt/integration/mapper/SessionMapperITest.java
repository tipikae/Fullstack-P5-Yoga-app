package com.openclassrooms.starterjwt.integration.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionMapperITest {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private static User user;
    private static Teacher teacher;

    @BeforeAll
    void setUp() {
        user = userRepository.save(new User(null, "test@test.com", "user-lastname", "user-firstname",
                "123456", false, LocalDateTime.now(), null));
        teacher = teacherRepository.save(new Teacher(null, "teacher-lastname", "teacher-firstname", LocalDateTime.now(), null));
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteById(user.getId());
        teacherRepository.deleteById(teacher.getId());
    }

    @Test
    void test() {
        Session session = new Session(1L, "session-name", new Date(), "session-desc", teacher,
                Arrays.asList(user), LocalDateTime.now(), null);
        // toDto
        SessionDto sessionDto = sessionMapper.toDto(session);
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        // toEntity
        assertEquals(sessionDto.getName(), sessionMapper.toEntity(sessionDto).getName());

        List<Session> sessions = Arrays.asList(session);
        // toDto
        List<SessionDto> sessionDtos = sessionMapper.toDto(sessions);
        assertTrue(sessionDtos.size() == 1);
        assertEquals(sessions.get(0).getDescription(), sessionDtos.get(0).getDescription());
        // toEntity
        assertTrue(sessionMapper.toEntity(sessionDtos).size() == 1);
        assertEquals(sessionDtos.get(0).getCreatedAt(), sessionMapper.toEntity(sessionDtos).get(0).getCreatedAt());
    }
}
