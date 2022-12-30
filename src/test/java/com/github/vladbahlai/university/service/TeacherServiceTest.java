package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.model.Teacher;
import com.github.vladbahlai.university.repository.DisciplineRepository;
import com.github.vladbahlai.university.repository.TeacherRepository;
import com.github.vladbahlai.university.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TeacherServiceImpl.class)
class TeacherServiceTest {

    @MockBean
    TeacherRepository teacherRepo;

    @MockBean
    DisciplineRepository disciplineRepo;
    @MockBean
    PasswordEncoder passwordEncoder;
    @Autowired
    TeacherService service;

    @Test
    void shouldAddDisciplineToTeacher() {
        Specialty specialty = new Specialty(1L, "test");
        Teacher teacher = new Teacher(1L, "name", "password");
        Discipline discipline = new Discipline(1L, "name", 2.0, 15, Course.FIRST, specialty);

        when(teacherRepo.findById(1L)).thenReturn(Optional.of(teacher));
        when(disciplineRepo.findById(1L)).thenReturn(Optional.of(discipline));

        Teacher actual = service.addDisciplineToTeacher(1L, 1L);
        Teacher expected = new Teacher(1L, "name", "password");
        expected.getDisciplines().add(discipline);

        assertEquals(expected, actual);

    }

    @Test
    void shouldNotAddDisciplineToTeacherWhenTeacherHaveDiscipline() {
        Specialty specialty = new Specialty(1L, "test");
        Teacher teacher = new Teacher(1L, "name", "password");
        Discipline discipline = new Discipline(1L, "name", 2.0, 15, Course.FIRST, specialty);
        teacher.getDisciplines().add(discipline);

        when(teacherRepo.findById(1L)).thenReturn(Optional.of(teacher));
        when(disciplineRepo.findById(1L)).thenReturn(Optional.of(discipline));

        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> service.addDisciplineToTeacher(1L, 1L));
        String expected = "Could not add discipline to teacher";
        assertEquals(expected, thrown.getMessage());

    }

    @Test
    void shouldNotAddDisciplineToTeacherWhenTeacherDisciplineNull() {
        Teacher teacher = new Teacher(1L, "name", "password");

        when(teacherRepo.findById(1L)).thenReturn(Optional.of(teacher));
        when(disciplineRepo.findById(1L)).thenReturn(Optional.empty());

        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> service.addDisciplineToTeacher(1L, 1L));
        String expected = "Could not add discipline to teacher";
        assertEquals(expected, thrown.getMessage());

    }

    @Test
    void shouldRemoveDisciplineFromTeacher() {
        Specialty specialty = new Specialty(1L, "test");
        Teacher teacher = new Teacher(1L, "name", "password");
        Discipline discipline = new Discipline(1L, "name", 2.0, 15, Course.FIRST, specialty);
        teacher.getDisciplines().add(discipline);

        when(teacherRepo.findById(1L)).thenReturn(Optional.of(teacher));
        when(disciplineRepo.findById(1L)).thenReturn(Optional.of(discipline));

        Teacher actual = service.deleteDisciplineFromTeacher(1L, 1L);
        Teacher expected = new Teacher(1L, "name", "password");

        assertEquals(expected.getDisciplines(), actual.getDisciplines());

    }

    @Test
    void shouldNotRemoveDisciplineFromTeacherWhenTeacherDoesntHaveDiscipline() {
        Specialty specialty = new Specialty(1L, "test");
        Teacher teacher = new Teacher(1L, "name", "password");
        Discipline discipline = new Discipline(1L, "name", 2.0, 15, Course.FIRST, specialty);

        when(teacherRepo.findById(1L)).thenReturn(Optional.of(teacher));
        when(disciplineRepo.findById(1L)).thenReturn(Optional.of(discipline));

        Throwable thrown = assertThrows(IllegalArgumentException.class,
                () -> service.deleteDisciplineFromTeacher(1L, 1L));
        String expected = "Could not remove discipline from teacher";

        assertEquals(expected, thrown.getMessage());

    }

    @Test
    void shouldNotRemoveDisciplineToTeacherWhenTeacherOrDisciplineNull() {
        Teacher teacher = new Teacher(1L, "name", "password");

        when(teacherRepo.findById(1L)).thenReturn(Optional.of(teacher));
        when(disciplineRepo.findById(1L)).thenReturn(Optional.empty());

        Throwable thrown = assertThrows(IllegalArgumentException.class,
                () -> service.deleteDisciplineFromTeacher(1L, 1L));
        String expected = "Could not remove discipline from teacher";

        assertEquals(expected, thrown.getMessage());

    }

    @Test
    void shouldCreateTeacher() throws UniqueNameConstraintException {
        Teacher teacher = new Teacher("test", "test");
        Teacher expected = new Teacher(1L, "test", "test");
        when(teacherRepo.existsByName(teacher.getName())).thenReturn(false);
        when(teacherRepo.save(teacher)).thenReturn(new Teacher(1L, "test", "test"));
        Teacher actual = service.saveTeacher(teacher);
        verify(passwordEncoder, times(1)).encode(anyString());
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateTeacherWithPasswordEncode() throws UniqueNameConstraintException {
        Teacher expected = new Teacher(1L, "test", "test");
        Teacher teacher = new Teacher(1L, "test", "123");

        when(teacherRepo.findById(expected.getId())).thenReturn(Optional.of(teacher));
        when(teacherRepo.existsByName(expected.getName())).thenReturn(true);
        when(teacherRepo.existsById(expected.getId())).thenReturn(true);
        when(teacherRepo.save(expected)).thenReturn(expected);
        Teacher actual = service.saveTeacher(expected);
        assertEquals(expected, actual);
        verify(passwordEncoder, times(1)).encode(anyString());

    }

    @Test
    void shouldUpdateTeacherWithoutPasswordEncode() throws UniqueNameConstraintException {
        Teacher expected = new Teacher(1L, "test", "test");

        when(teacherRepo.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(teacherRepo.existsByName(expected.getName())).thenReturn(true);
        when(teacherRepo.existsById(expected.getId())).thenReturn(true);
        when(teacherRepo.save(expected)).thenReturn(expected);
        Teacher actual = service.saveTeacher(expected);
        assertEquals(expected, actual);
        verify(passwordEncoder, times(0)).encode(anyString());

    }


    @Test
    void shouldThrowUniqueNameConstraintException() {
        Teacher firstTeacher = new Teacher("test", "test");
        Teacher secondTeacher = new Teacher(1L, "test", "test");
        when(teacherRepo.existsByName(firstTeacher.getName())).thenReturn(true);
        Exception firstException = assertThrows(UniqueNameConstraintException.class, () -> service.saveTeacher(firstTeacher));
        when(teacherRepo.existsByName(secondTeacher.getName())).thenReturn(true);
        when(teacherRepo.existsById(secondTeacher.getId())).thenReturn(true);
        when(teacherRepo.findById(secondTeacher.getId())).thenReturn(Optional.of(new Teacher(1L, "test2", "test")));
        Exception secondException = assertThrows(UniqueNameConstraintException.class, () -> service.saveTeacher(secondTeacher));
        String expectedExceptionMessage = "Teacher with test name already exist.";
        assertEquals(expectedExceptionMessage, firstException.getMessage());
        assertEquals(expectedExceptionMessage, secondException.getMessage());


    }
}