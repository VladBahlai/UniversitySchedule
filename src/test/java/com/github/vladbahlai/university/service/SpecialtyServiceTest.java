package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.repository.SpecialtyRepository;
import com.github.vladbahlai.university.service.impl.SpecialtyServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SpecialtyServiceImpl.class)
class SpecialtyServiceTest {

    @MockBean
    SpecialtyRepository repository;
    @Autowired
    SpecialtyService service;

    @Test
    void shouldCreateSpecialty() throws UniqueNameConstraintException {
        Specialty specialty = new Specialty("1");
        Specialty expected = new Specialty(1L, "1");
        when(repository.existsByName(specialty.getName())).thenReturn(false);
        when(repository.save(specialty)).thenReturn(new Specialty(1L, "1"));
        Specialty actual = service.saveSpecialty(specialty);
        assertEquals(expected, actual);

    }

    @Test
    void shouldUpdateSpecialty() throws UniqueNameConstraintException {
        Specialty expected = new Specialty(1L, "1");
        when(repository.existsByName(expected.getName())).thenReturn(true);
        when(repository.existsById(expected.getId())).thenReturn(true);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(repository.save(expected)).thenReturn(expected);
        Specialty actual = service.saveSpecialty(expected);
        assertEquals(expected, actual);

    }

    @Test
    void shouldThrowUniqueNameConstraintException() {
        Specialty firstSpecialty = new Specialty("a");
        Specialty secondSpecialty = new Specialty(1L, "a");
        when(repository.existsByName(firstSpecialty.getName())).thenReturn(true);
        Exception firstException = assertThrows(UniqueNameConstraintException.class, () -> service.saveSpecialty(firstSpecialty));
        when(repository.existsByName(secondSpecialty.getName())).thenReturn(true);
        when(repository.existsById(secondSpecialty.getId())).thenReturn(true);
        when(repository.findById(secondSpecialty.getId())).thenReturn(Optional.of(new Specialty(1L, "2")));
        Exception secondException = assertThrows(UniqueNameConstraintException.class, () -> service.saveSpecialty(secondSpecialty));
        String expectedExceptionMessage = "Specialty with a name already exist.";
        assertEquals(expectedExceptionMessage, firstException.getMessage());
        assertEquals(expectedExceptionMessage, secondException.getMessage());

    }
}
