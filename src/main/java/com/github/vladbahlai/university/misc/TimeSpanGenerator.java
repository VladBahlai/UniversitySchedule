package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.TimeSpan;
import com.github.vladbahlai.university.service.TimeSpanService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;

@Service
public class TimeSpanGenerator {

    private final TimeSpanService timeSpanService;

    public TimeSpanGenerator(TimeSpanService timeSpanService) {
        this.timeSpanService = timeSpanService;
    }

    public void generateTimeSpanData(int countOfLessons, LocalTime startTimeOfLesson, Duration durationOfLesson, Duration recess) throws UniqueConstraintException {
        for (int i = 1; i <= countOfLessons; i++) {
            LocalTime startTime = startTimeOfLesson;
            LocalTime endTime = startTime.plus(durationOfLesson);
            timeSpanService.saveTimeSpan(new TimeSpan(i, startTime, endTime));
            startTimeOfLesson = endTime.plus(recess);
        }
    }
}
