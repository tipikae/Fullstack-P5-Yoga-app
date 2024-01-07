package com.openclassrooms.starterjwt.integration.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionRepositoryIT {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void test() {
        User user = userRepository.save(new User(1L, "test@test.com", "user-firstname",
                "user-lastname", "123456", false, LocalDateTime.now(), null));
        Teacher teacher = teacherRepository.save(new Teacher(1L, "teacher-firstname",
                "teacher-lastname", LocalDateTime.now(), null));
        Session session = new Session();
        session.setName("session-name");
        session.setUsers(Arrays.asList(user));
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setDescription("session description");
        session.setCreatedAt(LocalDateTime.now());

        // save
        Session sessionSaved = sessionRepository.save(session);
        assertNotNull(sessionSaved);
        assertEquals(session.getDescription(), sessionSaved.getDescription());

        // get all
        assertTrue(sessionRepository.findAll().size() > 0);

        // get one
        assertEquals(session.getName(), sessionRepository.findById(session.getId()).get().getName());

        // update
        sessionSaved.setName(session.getName() + "-update");
        sessionRepository.save(sessionSaved);
        assertEquals(sessionSaved.getName(), sessionRepository.findById(session.getId()).get().getName());

        // delete
        sessionRepository.deleteById(sessionSaved.getId());
        assertFalse(sessionRepository.existsById(session.getId()));
    }
}
