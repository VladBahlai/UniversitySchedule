package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = LessonGenerator.class)
class LessonGeneratorTest {

    @MockBean
    LessonService lessonService;
    @MockBean
    AudienceService audienceService;
    @MockBean
    GroupService groupService;
    @MockBean
    TimeSpanService timeSpanService;
    @MockBean
    TeacherService teacherService;
    @Autowired
    LessonGenerator generator;

    @Test
    void shouldNotGenerateLessonsTeacherDoesntHaveSuitableDisciplines() {
        List<Teacher> teachers = new ArrayList<>(
                Arrays.asList(
                        new Teacher(1L, "Test", "123","email@example.com" ,new Department(1L, "test")),
                        new Teacher(2L, "Test", "123","email2@example.com", new Department(2L, "test"))));
        teachers.get(0).getDisciplines().add(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty()));
        teachers.get(0).getDisciplines().add(
                new Discipline(2L, "test", 3.0, 120, Course.FIRST, new Specialty()));

        teachers.get(1).getDisciplines().add(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty()));
        teachers.get(1).getDisciplines().add(
                new Discipline(2L, "test", 3.0, 120, Course.FIRST, new Specialty()));


        List<Audience> audiences = new ArrayList<>(Arrays.asList(new Audience(), new Audience(), new Audience()));
        List<Group> groups = new ArrayList<>(Arrays.asList(new Group("test", Course.THIRD), new Group("test", Course.THIRD)));
        List<TimeSpan> timeSpans = new ArrayList<>(Arrays.asList(new TimeSpan()));

        when(timeSpanService.getTimeSpans()).thenReturn(timeSpans);
        when(teacherService.getTeachers()).thenReturn(teachers);
        when(audienceService.getAudiences()).thenReturn(audiences);
        when(groupService.getGroups()).thenReturn(groups);
        generator.generateLessonDate(LocalDate.of(2022, 10, 17), 1);
        verify(lessonService, times(0)).saveLesson(any(Lesson.class));
    }

    @Test
    void shouldGenerateLessons() {
        List<Teacher> teachers = new ArrayList<>(
                Arrays.asList(
                        new Teacher(1L, "Test", "123","email@example.com" ,new Department(1L, "test")),
                        new Teacher(2L, "Test", "123","email2@example.com", new Department(2L, "test"))));
        teachers.get(0).getDisciplines().add(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty()));
        teachers.get(0).getDisciplines().add(
                new Discipline(2L, "test", 3.0, 120, Course.FIRST, new Specialty()));

        teachers.get(1).getDisciplines().add(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty()));
        teachers.get(1).getDisciplines().add(
                new Discipline(2L, "test", 3.0, 120, Course.FIRST, new Specialty()));


        List<Audience> audiences = new ArrayList<>(Arrays.asList(new Audience("123"), new Audience("123"), new Audience("123")));
        List<Group> groups = new ArrayList<>(Arrays.asList(new Group("test", Course.FIRST), new Group("test", Course.FIRST)));
        List<TimeSpan> timeSpans = new ArrayList<>(Arrays.asList(new TimeSpan()));

        when(timeSpanService.getTimeSpans()).thenReturn(timeSpans);
        when(teacherService.getTeachers()).thenReturn(teachers);
        when(audienceService.getAudiences()).thenReturn(audiences);
        when(groupService.getGroups()).thenReturn(groups);
        generator.generateLessonDate(LocalDate.of(2022, 10, 17), 1);
        verify(lessonService, atMost(timeSpans.size() * groups.size())).saveLesson(any(Lesson.class));
    }

    @Test
    void shouldNotGenerateLessonOnWeekend() {
        generator.generateLessonDate(LocalDate.of(2022, 10, 8), 2);
        verify(lessonService, times(0)).saveLesson(any(Lesson.class));
    }
}
