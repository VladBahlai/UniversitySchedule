package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.model.Teacher;
import com.github.vladbahlai.university.service.RoleService;
import com.github.vladbahlai.university.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TeacherGenerator.class)
class TeacherGeneratorTest {

    @MockBean
    TeacherService teacherService;
    @MockBean
    RoleService roleService;
    @Autowired
    TeacherGenerator generator;

    @Test
    void shouldGenerateTeachers() throws UniqueConstraintException {
        List<Department> departments = new ArrayList<>(Arrays.asList(new Department("Department")));
        departments.get(0).getSpecialties().add(new Specialty("Specialty"));
        generator.generateTeacherData(departments, 7);
        verify(teacherService, times(7 * departments.size())).saveTeacher(any(Teacher.class));
        verify(roleService, times(1)).getRoleByName("ROLE_TEACHER");
    }

    @Test
    void shouldGenerateTeacherDisciplines() {
        Department department = new Department(1L, "testDepartment");
        List<Teacher> teacherList = new ArrayList<>(Arrays.asList(new Teacher(1L, "Adam", "12345","email@example.com" ,department)));
        List<Discipline> disciplineList =
                new ArrayList<>(Arrays.asList(
                        new Discipline("disc1", 4.0, 120, Course.FIRST, new Specialty(1L, "specialty1", department)),
                        new Discipline("disc2", 4.0, 120, Course.FIRST, new Specialty(2L, "specialty2", department)),
                        new Discipline("disc3", 4.0, 120, Course.FIRST, new Specialty(3L, "specialty3", department)),
                        new Discipline("disc4", 4.0, 120, Course.FIRST, new Specialty(4L, "specialty4", department))
                ));

        generator.generateTeacherDisciplines(teacherList, disciplineList, 4);
        verify(teacherService, atMost(4)).addDisciplineToTeacher(any(Long.class), any(Long.class));
    }

    @Test
    void shouldNotGenerateTeacherDisciplinesWhenDifferentDepartments() {
        Department department = new Department(1L, "testDepartment");
        List<Teacher> teacherList = new ArrayList<>(Arrays.asList(new Teacher(1L, "Adam", "12345","email@example.com" ,department)));
        List<Discipline> disciplineList =
                new ArrayList<>(Arrays.asList(
                        new Discipline("disc1", 4.0, 120, Course.FIRST, new Specialty(1L, "specialty1", department)),
                        new Discipline("disc2", 4.0, 120, Course.FIRST, new Specialty(2L, "specialty2", department)),
                        new Discipline("disc3", 4.0, 120, Course.FIRST, new Specialty(3L, "specialty3", department)),
                        new Discipline("disc4", 4.0, 120, Course.FIRST, new Specialty(4L, "specialty4", department))
                ));

        generator.generateTeacherDisciplines(teacherList, disciplineList, 4);
        verify(teacherService, times(0)).addDisciplineToTeacher(any(Long.class), any(Long.class));
    }

}
