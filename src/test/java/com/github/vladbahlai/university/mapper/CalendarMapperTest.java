package com.github.vladbahlai.university.mapper;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.enums.TypeOfLesson;
import com.github.vladbahlai.university.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CalendarMapper.class)
class CalendarMapperTest {

    @Autowired
    CalendarMapper mapper;

    @Test
    void shouldReturnCalendarDTO() {
        Specialty specialty = new Specialty("test");
        Discipline discipline = new Discipline("math", 2.0, 15, Course.THIRD, specialty);
        Group group = new Group("PA-32", Course.THIRD, specialty);
        Teacher teacher = new Teacher("teacher", "1234","email@example.com");
        TimeSpan timeSpan = new TimeSpan(1, LocalTime.of(10, 43, 12), LocalTime.of(10, 45, 12));
        Audience audience = new Audience("test");
        Lesson lesson = new Lesson(discipline, group, teacher, LocalDate.of(2020, 1, 8), timeSpan,
                audience, TypeOfLesson.PRACTICE);

        CalendarDTO actual = mapper.toCalendarDTO(lesson);
        CalendarDTO expected = new CalendarDTO("math", "2020-01-08 10:43:12", "2020-01-08 10:45:12", "math[PRACTICE]<br>Aud. test<br>teacher<br>PA-32");
        assertEquals(expected, actual);
    }
}
