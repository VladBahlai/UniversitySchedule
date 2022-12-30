package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.model.Student;
import com.github.vladbahlai.university.service.RoleService;
import com.github.vladbahlai.university.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = StudentGenerator.class)
class StudentGeneratorTest {

    @MockBean
    StudentService studentService;
    @MockBean
    RoleService roleService;
    @Autowired
    StudentGenerator generator;

    @Test
    void shouldGenerateStudents() throws UniqueNameConstraintException {
        List<Group> groups = new ArrayList<>(Arrays.asList(new Group(), new Group()));
        generator.generateStudentData(groups, 5, 15);
        verify(studentService, atLeast(5 * groups.size())).saveStudent(any(Student.class));
        verify(studentService, atMost(15 * groups.size())).saveStudent(any(Student.class));
        verify(roleService, times(1)).getRoleByName("ROLE_STUDENT");
    }
}
