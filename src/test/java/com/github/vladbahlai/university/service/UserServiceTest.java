package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
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
public class UserServiceTest {

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
    void shouldCreateUser() throws UniqueNameConstraintException {
        User user = new User("test", "test");
        User expected = new User(1L, "test", "test");
        when(repository.existsByName(user.getName())).thenReturn(false);
        when(repository.save(user)).thenReturn(new User(1L, "test", "test"));
        User actual = service.saveUser(user);
        assertEquals(expected, actual);
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void shouldUpdateUserWithoutPasswordEncode() throws UniqueNameConstraintException {
        User expected = new User(1L, "test", "test");
        when(repository.existsByName(expected.getName())).thenReturn(true);
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
    void shouldUpdateUserWithPasswordEncode() throws UniqueNameConstraintException {
        User expected = new User(1L, "test", "test");
        User user = new User(1L, "test", "test123");
        when(repository.existsByName(expected.getName())).thenReturn(true);
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
        User firstUser = new User("test", "test");
        User secondUser = new User(1L, "test", "test");
        when(repository.existsByName(firstUser.getName())).thenReturn(true);
        Exception firstException = assertThrows(UniqueNameConstraintException.class, () -> service.saveUser(firstUser));
        when(repository.existsByName(secondUser.getName())).thenReturn(true);
        when(repository.existsById(secondUser.getId())).thenReturn(true);
        when(repository.findById(secondUser.getId())).thenReturn(Optional.of(new User(1L, "test2", "test")));
        Exception secondException = assertThrows(UniqueNameConstraintException.class, () -> service.saveUser(secondUser));
        String expectedExceptionMessage = "User with test name already exist.";
        assertEquals(expectedExceptionMessage, firstException.getMessage());
        assertEquals(expectedExceptionMessage, secondException.getMessage());

    }

}
