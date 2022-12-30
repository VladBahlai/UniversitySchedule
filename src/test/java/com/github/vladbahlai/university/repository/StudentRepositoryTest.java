package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository repo;

    @Test
    void shouldReadAndWriteStudent() {
        Student expected = repo.save(new Student("student", "123"));
        Optional<Student> actual = repo.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

    }

}
