package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Student;
import com.github.vladbahlai.university.repository.StudentRepository;
import com.github.vladbahlai.university.service.impl.StudentServiceImpl;
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

@SpringBootTest(classes = StudentServiceImpl.class)
class StudentServiceTest {

    @MockBean
    StudentRepository repository;
    @MockBean
    PasswordEncoder passwordEncoder;
    @Autowired
    StudentService service;

    @Test
    void shouldCreateStudent() throws UniqueNameConstraintException {
        Student student = new Student("test", "test");
        Student expected = new Student(1L, "test", "test");
        when(repository.existsByName(student.getName())).thenReturn(false);
        when(repository.save(student)).thenReturn(new Student(1L, "test", "test"));
        Student actual = service.saveStudent(student);
        assertEquals(expected, actual);
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void shouldUpdateStudentWithPasswordEncode() throws UniqueNameConstraintException {
        Student expected = new Student(1L, "test", "test");
        Student student = new Student(1L, "test", "123");
        when(repository.findById(expected.getId())).thenReturn(Optional.of(student));
        when(repository.existsByName(expected.getName())).thenReturn(true);
        when(repository.existsById(expected.getId())).thenReturn(true);
        when(repository.save(expected)).thenReturn(expected);
        Student actual = service.saveStudent(expected);
        verify(passwordEncoder, times(1)).encode(anyString());
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateStudentWithoutPasswordEncode() throws UniqueNameConstraintException {
        Student expected = new Student(1L, "test", "test");
        when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(repository.existsByName(expected.getName())).thenReturn(true);
        when(repository.existsById(expected.getId())).thenReturn(true);
        when(repository.save(expected)).thenReturn(expected);
        Student actual = service.saveStudent(expected);
        verify(passwordEncoder, times(0)).encode(anyString());
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowUniqueNameConstraintException() {
        Student firstStudent = new Student("test", "test");
        Student secondStudent = new Student(1L, "test", "test");
        when(repository.existsByName(firstStudent.getName())).thenReturn(true);
        Exception firstException = assertThrows(UniqueNameConstraintException.class, () ->
                service.saveStudent(firstStudent)
        );
        when(repository.existsByName(secondStudent.getName())).thenReturn(true);
        when(repository.existsById(secondStudent.getId())).thenReturn(true);
        when(repository.findById(secondStudent.getId())).thenReturn(Optional.of(new Student(1L, "test2", "test")));
        Exception secondException = assertThrows(UniqueNameConstraintException.class, () ->
                service.saveStudent(secondStudent)
        );
        String expectedExceptionMessage = "Student with test name already exist.";
        assertEquals(expectedExceptionMessage, firstException.getMessage());
        assertEquals(expectedExceptionMessage, secondException.getMessage());

    }
}
