package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TeacherRepositoryTest {

    @Autowired
    TeacherRepository repo;

    @Test
    void shouldReadAndWriteTeacher() {
        Teacher expected = repo.save(new Teacher("teacher", "1234"));
        Optional<Teacher> actual = repo.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

    }

}
