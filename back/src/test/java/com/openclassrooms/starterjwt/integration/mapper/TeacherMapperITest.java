package com.openclassrooms.starterjwt.integration.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TeacherMapperITest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void test() {
        Teacher teacher = new Teacher(1L, "teacher-lastname", "teacher-firstname", LocalDateTime.now(), null);
        TeacherDto teacherDto = teacherMapper.toDto(teacher);
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacherDto.getLastName(), teacherMapper.toEntity(teacherDto).getLastName());

        List<Teacher> teachers = Arrays.asList(teacher);
        List<TeacherDto> teacherDtos = teacherMapper.toDto(teachers);
        assertTrue(teacherDtos.size() == 1);
        assertEquals(teachers.get(0).getFirstName(), teacherDtos.get(0).getFirstName());
        assertTrue(teacherMapper.toEntity(teacherDtos).size() == 1);
        assertEquals(teacherDtos.get(0).getCreatedAt(), teacherMapper.toEntity(teacherDtos).get(0).getCreatedAt());
    }
}
