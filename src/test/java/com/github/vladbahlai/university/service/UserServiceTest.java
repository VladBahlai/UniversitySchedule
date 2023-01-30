package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.User;
import com.github.vladbahlai.university.repository.StudentRepository;
import com.github.vladbahlai.university.repository.TeacherRepository;
import com.github.vladbahlai.university.repository.UserRepository;
import com.github.vladbahlai.university.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceTest {

    @MockBean
    UserRepository repository;
    @MockBean
    TeacherRepository teacherRepository;
    @MockBean
    StudentRepository studentRepository;
    @MockBean
    PasswordEncoder passwordEncoder;
    @Qualifier("userServiceImpl")
    @Autowired
    UserService service;

    @Test
    void shouldCreateUser() throws UniqueConstraintException {
        User user = new User("test", "test", "email@example.com");
        User expected = new User(1L, "test", "test", "email@example.com");
        when(repository.existsByEmail(user.getEmail())).thenReturn(false);
        when(repository.save(user)).thenReturn(new User(1L, "test", "test", "email@example.com"));
        User actual = service.saveUser(user);
        assertEquals(expected, actual);
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void shouldUpdateUserWithoutPasswordEncode() throws UniqueConstraintException {
        User expected = new User(1L, "test", "test", "email@example.com");
        when(repository.existsByEmail(expected.getEmail())).thenReturn(true);
        when(repository.existsById(expected.getId())).thenReturn(true);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(teacherRepository.existsById(expected.getId())).thenReturn(false);
        when(studentRepository.existsById(expected.getId())).thenReturn(false);
        when(repository.save(expected)).thenReturn(expected);
        User actual = service.saveUser(expected);
        assertEquals(expected, actual);
        verify(passwordEncoder, times(0)).encode(anyString());
    }

    @Test
    void shouldUpdateUserWithPasswordEncode() throws UniqueConstraintException {
        User expected = new User(1L, "test", "test", "email@example.com");
        User user = new User(1L, "test", "test123", "email@example.com");
        when(repository.existsByEmail(expected.getEmail())).thenReturn(true);
        when(repository.existsById(expected.getId())).thenReturn(true);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(user));
        when(teacherRepository.existsById(expected.getId())).thenReturn(false);
        when(studentRepository.existsById(expected.getId())).thenReturn(false);
        when(repository.save(expected)).thenReturn(expected);
        User actual = service.saveUser(expected);
        assertEquals(expected, actual);
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void shouldThrowUniqueNameConstraintException() {
        User firstUser = new User("test", "test", "email@example.com");
        User secondUser = new User(1L, "test", "test", "email@example.com");
        when(repository.existsByEmail(firstUser.getEmail())).thenReturn(true);
        Exception firstException = assertThrows(UniqueConstraintException.class, () -> service.saveUser(firstUser));

        when(repository.existsByEmail(secondUser.getEmail())).thenReturn(true);
        when(repository.existsById(secondUser.getId())).thenReturn(true);
        when(repository.findById(secondUser.getId())).thenReturn(Optional.of(new User(1L, "test", "test", "emaial@example.com")));
        Exception secondException = assertThrows(UniqueConstraintException.class, () -> service.saveUser(secondUser));
        String expectedExceptionMessage = "User with email@example.com email already exist.";
        assertEquals(expectedExceptionMessage, firstException.getMessage());
        assertEquals(expectedExceptionMessage, secondException.getMessage());

    }

}
