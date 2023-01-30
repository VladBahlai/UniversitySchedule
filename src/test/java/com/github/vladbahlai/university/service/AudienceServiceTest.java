package com.github.vladbahlai.university.service;


import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.model.TimeSpan;
import com.github.vladbahlai.university.repository.AudienceRepository;
import com.github.vladbahlai.university.service.impl.AudienceServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AudienceServiceImpl.class)
class AudienceServiceTest {

    @MockBean
    AudienceRepository repository;
    @Autowired
    AudienceService service;

    @Test
    void shouldReturnFreeAudiences() {
        List<Audience> busyAudiences = new ArrayList<>(Arrays.asList(
                new Audience(1L, "1"),
                new Audience(2L, "2")));
        List<Audience> allAudiences = new ArrayList<>(Arrays.asList(
                new Audience(1L, "1"),
                new Audience(2L, "2"),
                new Audience(3L, "3"),
                new Audience(4L, "4"),
                new Audience(5L, "5")));
        List<Audience> expected = new ArrayList<>(Arrays.asList(
                new Audience(3L, "3"),
                new Audience(4L, "4"),
                new Audience(5L, "5")));
        LocalDate date = LocalDate.of(2022, 10, 12);
        TimeSpan timeSpan = new TimeSpan(1L, 1, LocalTime.of(10, 12), LocalTime.of(12, 12));

        when(repository.findAudiencesByDateAndTimeSpan(date, timeSpan)).thenReturn(busyAudiences);
        when(repository.findAll()).thenReturn(allAudiences);

        List<Audience> actual = service.getFreeAudiences(date, timeSpan);
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateAudience() throws UniqueConstraintException {
        Audience audience = new Audience("1");
        Audience expected = new Audience(1L, "1");
        when(repository.existsByName(audience.getName())).thenReturn(false);
        when(repository.save(audience)).thenReturn(new Audience(1L, "1"));
        Audience actual = service.saveAudience(audience);
        assertEquals(expected, actual);

    }

    @Test
    void shouldUpdateAudience() throws UniqueConstraintException {
        Audience expected = new Audience(1L, "1");
        when(repository.existsByName(expected.getName())).thenReturn(true);
        when(repository.existsById(expected.getId())).thenReturn(true);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(repository.save(expected)).thenReturn(expected);
        Audience actual = service.saveAudience(expected);
        assertEquals(expected, actual);

    }

    @Test
    void shouldThrowUniqueNameConstraintException() {
        Audience firstAudience = new Audience("a");
        Audience secondAudience = new Audience(1L, "a");
        when(repository.existsByName(firstAudience.getName())).thenReturn(true);
        Exception firstException = assertThrows(UniqueConstraintException.class, () -> service.saveAudience(firstAudience));
        when(repository.existsByName(secondAudience.getName())).thenReturn(true);
        when(repository.existsById(secondAudience.getId())).thenReturn(true);
        when(repository.findById(secondAudience.getId())).thenReturn(Optional.of(new Audience(1L, "2")));
        Exception secondException = assertThrows(UniqueConstraintException.class, () -> service.saveAudience(secondAudience));
        String expectedExceptionMessage = "Audience with a name already exist.";
        assertEquals(expectedExceptionMessage, firstException.getMessage());
        assertEquals(expectedExceptionMessage, secondException.getMessage());

    }

}
