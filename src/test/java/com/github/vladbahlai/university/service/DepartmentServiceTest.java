package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.repository.DepartmentRepository;
import com.github.vladbahlai.university.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = DepartmentServiceImpl.class)
class DepartmentServiceTest {

    @MockBean
    DepartmentRepository repository;
    @Autowired
    DepartmentService service;

    @Test
    void shouldCreateDepartment() throws UniqueConstraintException {
        Department department = new Department("1");
        Department expected = new Department(1L, "1");
        when(repository.existsByName(department.getName())).thenReturn(false);
        when(repository.save(department)).thenReturn(new Department(1L, "1"));
        Department actual = service.saveDepartment(department);
        assertEquals(expected, actual);

    }

    @Test
    void shouldUpdateDepartment() throws UniqueConstraintException {
        Department expected = new Department(1L, "1");
        when(repository.existsByName(expected.getName())).thenReturn(true);
        when(repository.existsById(expected.getId())).thenReturn(true);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(repository.save(expected)).thenReturn(expected);
        Department actual = service.saveDepartment(expected);
        assertEquals(expected, actual);

    }

    @Test
    void shouldThrowUniqueNameConstraintException() {
        Department firstDepartment = new Department("a");
        Department secondDepartment = new Department(1L, "a");
        when(repository.existsByName(firstDepartment.getName())).thenReturn(true);
        Exception firstException = assertThrows(UniqueConstraintException.class, () -> service.saveDepartment(firstDepartment));
        when(repository.existsByName(secondDepartment.getName())).thenReturn(true);
        when(repository.existsById(secondDepartment.getId())).thenReturn(true);
        when(repository.findById(secondDepartment.getId())).thenReturn(Optional.of(new Department(1L, "2")));
        Exception secondException = assertThrows(UniqueConstraintException.class, () -> service.saveDepartment(secondDepartment));
        String expectedExceptionMessage = "Department with a name already exist.";
        assertEquals(expectedExceptionMessage, firstException.getMessage());
        assertEquals(expectedExceptionMessage, secondException.getMessage());

    }
}
