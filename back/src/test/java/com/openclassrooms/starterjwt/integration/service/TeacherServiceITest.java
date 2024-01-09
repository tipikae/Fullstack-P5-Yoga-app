package com.openclassrooms.starterjwt.integration.service;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeacherServiceITest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeAll
    void setUp() {
        teacher = teacherRepository.save(new Teacher(null, "teacher-lastname", "teacher-firstname",
                LocalDateTime.now(), null));
    }

    @AfterAll
    void tearDown() {
        teacherRepository.deleteById(teacher.getId());
    }

    @Test
    @Order(1)
    void findByIdReturnsTeacherWhenFound() {
        assertEquals(teacher.getLastName(), teacherService.findById(teacher.getId()).getLastName());
    }

    @Test
    @Order(2)
    void findByIdReturnsNullWhenNotFound() {
        assertNull(teacherService.findById(10000L));
    }

    @Test
    @Order(3)
    void findAllReturnsList() {
        assertTrue(teacherService.findAll().size() > 0);
    }
}
