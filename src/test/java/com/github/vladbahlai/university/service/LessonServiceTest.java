package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.enums.TypeOfLesson;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.repository.LessonRepository;
import com.github.vladbahlai.university.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = LessonServiceImpl.class)
public class LessonServiceTest {

    @MockBean
    LessonRepository repository;
    @Autowired
    LessonService lessonService;

    @Test
    void shouldReturnLessonsFromDepartment() {
        Department department = new Department(1L, "test");
        List<Teacher> teacherList = Arrays.asList(new Teacher(1L, "test", "123"), new Teacher(2L, "test", "123"));
        department.getTeachers().addAll(teacherList);
        LocalDate start = LocalDate.of(2022, 12, 22);
        LocalDate end = LocalDate.of(2022, 12, 23);
        List<Lesson> lessonsFromFirstTeacher = Collections.singletonList(
                new Lesson(1L, new Discipline(1L, "test1", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(1L, "test1", Course.FIRST, new Specialty(1L, "test")), new Teacher(1L, "test1", "123"), LocalDate.of(2022, 12, 10),
                        new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)), new Audience(1L, "test1"), TypeOfLesson.PRACTICE));
        List<Lesson> lessonsFromSecondTeacher = Arrays.asList(
                new Lesson(2L, new Discipline(2L, "test2", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(2L, "test2", Course.FIRST, new Specialty(1L, "test")), new Teacher(2L, "test2", "123"), LocalDate.of(2022, 12, 10),
                        new TimeSpan(2L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)), new Audience(2L, "test2"), TypeOfLesson.PRACTICE),
                new Lesson(3L, new Discipline(3L, "test3", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(3L, "test3", Course.FIRST, new Specialty(1L, "test")), new Teacher(3L, "test3", "123"), LocalDate.of(2022, 12, 10),
                        new TimeSpan(3L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)), new Audience(3L, "test3"), TypeOfLesson.PRACTICE));
        when(repository.findByTeacherAndDateBetween(teacherList.get(0), start, end)).thenReturn(lessonsFromFirstTeacher);
        when(repository.findByTeacherAndDateBetween(teacherList.get(1), start, end)).thenReturn(lessonsFromSecondTeacher);
        List<Lesson> expected = new ArrayList<>();
        expected.addAll(lessonsFromFirstTeacher);
        expected.addAll(lessonsFromSecondTeacher);
        List<Lesson> actual = lessonService.getDepartmentLessons(department, start, end);
        assertEquals(expected, actual);
    }

}
