package com.github.vladbahlai.university.mapper;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.model.Lesson;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CalendarMapper {

    public CalendarDTO toCalendarDTO(Lesson lesson) {
        String title = lesson.getDiscipline().getName();
        String start = LocalDateTime.of(lesson.getDate(), lesson.getTimeSpan().getStartTime()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.of(lesson.getDate(), lesson.getTimeSpan().getEndTime()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String description = lesson.getDiscipline().getName() + "[" + lesson.getType().name() + "]" +
                "<br>" + "Aud. " + lesson.getAudience().getName() +
                "<br>" + lesson.getTeacher().getName() +
                "<br>" + lesson.getGroup().getName();
        return new CalendarDTO(title, start, end, description);
    }
}
