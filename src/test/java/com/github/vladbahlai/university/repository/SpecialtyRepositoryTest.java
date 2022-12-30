package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.Specialty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SpecialtyRepositoryTest {

    @Autowired
    SpecialtyRepository repo;

    @Test
    void shouldReadAndWriteSpecialty() {
        Specialty expected = repo.save(new Specialty("test"));
        Optional<Specialty> actual = repo.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

    }

}
