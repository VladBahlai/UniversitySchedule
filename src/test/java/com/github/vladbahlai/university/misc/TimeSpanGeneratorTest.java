package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.TimeSpan;
import com.github.vladbahlai.university.service.TimeSpanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = TimeSpanGenerator.class)
class TimeSpanGeneratorTest {

    @MockBean
    TimeSpanService service;

    @Autowired
    TimeSpanGenerator generator;

    @Test
    void shouldGenerateTimeSpans() throws UniqueConstraintException {
        generator.generateTimeSpanData(6, LocalTime.of(2, 34), Duration.ofMinutes(30), Duration.ofMinutes(30));
        verify(service, times(6)).saveTimeSpan(any(TimeSpan.class));
    }
}
