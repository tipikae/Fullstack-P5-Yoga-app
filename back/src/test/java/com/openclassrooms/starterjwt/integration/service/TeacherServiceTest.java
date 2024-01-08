package com.openclassrooms.starterjwt.integration.service;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private static Teacher teacher;

    @BeforeAll
    static void setUp() {
        teacher = new Teacher(1L, "lastName", "firstName", LocalDateTime.now(), null);
    }

    @Test
    void findAllReturnTeacherList() {
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher));
        assertEquals(1, teacherService.findAll().size());
        assertEquals(teacher.getLastName(), teacherService.findAll().get(0).getLastName());
    }

    @Test
    void findByIdReturnTeacherWhenFound() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        assertEquals(teacher.getFirstName(), teacherService.findById(teacher.getId()).getFirstName());
    }

    @Test
    void findByIdReturnNullWhenNotFound() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(teacherService.findById(teacher.getId()));
    }
}
