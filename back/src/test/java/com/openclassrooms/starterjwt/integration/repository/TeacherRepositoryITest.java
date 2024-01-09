package com.openclassrooms.starterjwt.integration.repository;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TeacherRepositoryITest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test()
    void test() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("teacher-firstname");
        teacher.setLastName("teacher-lastname");
        teacher.setCreatedAt(LocalDateTime.now());

        // save
        Teacher teacherSaved = teacherRepository.save(teacher);
        assertNotNull(teacherSaved);
        assertEquals(teacher.getFirstName(), teacherSaved.getFirstName());

        // get one
        assertEquals(teacher.getLastName(), teacherRepository.findById(teacherSaved.getId()).get().getLastName());

        // delete
        teacherRepository.deleteById(teacherSaved.getId());
        assertFalse(teacherRepository.existsById(teacherSaved.getId()));
    }
}
