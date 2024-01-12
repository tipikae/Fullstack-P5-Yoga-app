package com.openclassrooms.starterjwt.unit.controller;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class TeacherControllerTest {

    private static final String ROOT = "/api/teacher";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherMapper teacherMapper;

    @MockBean
    private TeacherService teacherService;

    private static Teacher teacher;
    private static TeacherDto teacherDto;

    @BeforeAll
    static void setUp() {
        teacher = new Teacher(1L, "teacher-lastname", "teacher-firstname", LocalDateTime.now(), null);
        teacherDto = new TeacherDto(teacher.getId(), teacher.getLastName(), teacher.getFirstName(), teacher.getCreatedAt(), teacher.getUpdatedAt());
    }

    @Test
    void findByIdReturns200AndTeacherWhenTeacherIsFound() throws Exception {
        when(teacherService.findById(anyLong())).thenReturn(teacher);
        when(teacherMapper.toDto(any(Teacher.class))).thenReturn(teacherDto);
        mockMvc.perform(get(ROOT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is(teacher.getLastName())));
    }

    @Test
    void findByIdReturns404WhenTeacherIsNotFound() throws Exception {
        when(teacherService.findById(anyLong())).thenReturn(null);
        mockMvc.perform(get(ROOT + "/1"))
                .andExpect(status().is(404));
    }

    @Test
    void findByIdReturns400WhenBadIdParameter() throws Exception {
        mockMvc.perform(get(ROOT + "/test"))
                .andExpect(status().is(400));
    }

    @Test
    void findAllReturns200AndList() throws Exception {
        when(teacherService.findAll()).thenReturn(Arrays.asList(teacher));
        when(teacherMapper.toDto(anyList())).thenReturn(Arrays.asList(teacherDto));
        mockMvc.perform(get(ROOT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName", is(teacher.getFirstName())));
    }
}
