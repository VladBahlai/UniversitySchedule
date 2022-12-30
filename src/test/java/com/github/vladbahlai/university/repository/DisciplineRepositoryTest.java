package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Specialty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class DisciplineRepositoryTest {

    @Autowired
    DisciplineRepository disciplineRepo;

    @Autowired
    SpecialtyRepository specialtyRepo;

    @Test
    void shouldReadAndWriteDiscipline() {
        Specialty specialty = specialtyRepo.save(new Specialty("test"));
        Discipline expected = disciplineRepo.save(new Discipline("math", 2.0, 15, Course.THIRD, specialty));
        Optional<Discipline> actual = disciplineRepo.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

    }

}
