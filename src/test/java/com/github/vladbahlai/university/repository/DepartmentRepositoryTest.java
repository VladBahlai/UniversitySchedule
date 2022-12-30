package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    DepartmentRepository repo;

    @Test
    void shouldReadAndWriteDepartment() {
        Department expected = repo.save(new Department("department"));
        Optional<Department> actual = repo.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

    }

}
