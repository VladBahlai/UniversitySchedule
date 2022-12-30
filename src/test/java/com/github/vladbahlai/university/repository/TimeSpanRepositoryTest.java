package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.TimeSpan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TimeSpanRepositoryTest {

    @Autowired
    TimeSpanRepository repo;

    @Test
    void shouldReadAndWriteTimeSpan() {
        TimeSpan expected = repo.save(new TimeSpan(1, LocalTime.of(10, 43, 12), LocalTime.of(10, 45, 12)));
        Optional<TimeSpan> actual = repo.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

    }

}
