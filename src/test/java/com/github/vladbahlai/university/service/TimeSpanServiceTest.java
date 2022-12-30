package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.TimeSpan;
import com.github.vladbahlai.university.repository.TimeSpanRepository;
import com.github.vladbahlai.university.service.impl.TimeSpanServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TimeSpanServiceImpl.class)
class TimeSpanServiceTest {

    @MockBean
    TimeSpanRepository repository;
    @Autowired
    TimeSpanService service;

    @Test
    void shouldCreateTimeSpan() throws UniqueNameConstraintException {
        TimeSpan timeSpan = new TimeSpan(1, LocalTime.of(12, 12), LocalTime.of(12, 12));
        TimeSpan expected = new TimeSpan(1L, 1, LocalTime.of(12, 12), LocalTime.of(12, 12));
        when(repository.existsByNumberOfLesson(timeSpan.getNumberOfLesson())).thenReturn(false);
        when(repository.save(timeSpan)).thenReturn(new TimeSpan(1L, 1, LocalTime.of(12, 12), LocalTime.of(12, 12)));
        TimeSpan actual = service.saveTimeSpan(timeSpan);
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateTimeSpan() throws UniqueNameConstraintException {
        TimeSpan expected = new TimeSpan(1L, 1, LocalTime.of(12, 12), LocalTime.of(12, 12));
        when(repository.existsByNumberOfLesson(expected.getNumberOfLesson())).thenReturn(true);
        when(repository.existsById(expected.getId())).thenReturn(true);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(repository.save(expected)).thenReturn(expected);
        TimeSpan actual = service.saveTimeSpan(expected);
        assertEquals(expected, actual);

    }

    @Test
    void shouldThrowUniqueNameConstraintException() {
        TimeSpan firstTimeSpan = new TimeSpan(1, LocalTime.of(12, 12), LocalTime.of(12, 12));
        TimeSpan secondTimeSpan = new TimeSpan(1L, 1, LocalTime.of(12, 12), LocalTime.of(12, 12));
        when(repository.existsByNumberOfLesson(firstTimeSpan.getNumberOfLesson())).thenReturn(true);
        Exception firstException = assertThrows(UniqueNameConstraintException.class, () -> service.saveTimeSpan(firstTimeSpan));
        when(repository.existsByNumberOfLesson(secondTimeSpan.getNumberOfLesson())).thenReturn(true);
        when(repository.existsById(secondTimeSpan.getId())).thenReturn(true);
        when(repository.findById(secondTimeSpan.getId())).thenReturn(Optional.of(new TimeSpan(1L, 2, LocalTime.of(12, 12), LocalTime.of(12, 12))));
        Exception secondException = assertThrows(UniqueNameConstraintException.class, () -> service.saveTimeSpan(secondTimeSpan));
        String expectedExceptionMessage = "TimeSpan with 1 number already exist.";
        assertEquals(expectedExceptionMessage, firstException.getMessage());
        assertEquals(expectedExceptionMessage, secondException.getMessage());

    }
}
