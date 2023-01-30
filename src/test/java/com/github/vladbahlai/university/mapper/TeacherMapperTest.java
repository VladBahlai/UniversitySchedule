package com.github.vladbahlai.university.mapper;

import com.github.vladbahlai.university.dto.TeacherDTO;
import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.model.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TeacherMapper.class)
public class TeacherMapperTest {

    @Autowired
    TeacherMapper teacherMapper;

    @Test
    void shouldReturnTeacherDTO(){
        Teacher teacher = new Teacher(1L,"user","password","email@example.com",new Department(1L,"test"));
        TeacherDTO expected = new TeacherDTO("1","user","1","test");
        TeacherDTO actual = teacherMapper.toTeacherDTO(teacher);
        assertEquals(expected,actual);
    }

}
