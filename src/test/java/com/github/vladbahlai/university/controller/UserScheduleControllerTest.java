package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.enums.TypeOfLesson;
import com.github.vladbahlai.university.mapper.CalendarMapper;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.security.MyUserDetails;
import com.github.vladbahlai.university.service.GroupService;
import com.github.vladbahlai.university.service.LessonService;
import com.github.vladbahlai.university.service.StudentService;
import com.github.vladbahlai.university.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserScheduleController.class)
class UserScheduleControllerTest {

    @MockBean
    StudentService studentService;
    @MockBean
    TeacherService teacherService;
    @MockBean
    LessonService lessonService;
    @MockBean
    CalendarMapper lessonMapper;
    @MockBean
    GroupService groupService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnTeacherScheduleView() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("WRITE_LESSON"))));
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenReturn(new Teacher(1L, "user", "user","email@example.com"));
        when(studentService.getStudentById(userDetails.getUser().getId())).thenThrow(IllegalArgumentException.class);
        this.mockMvc
                .perform(get("/mySchedule").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("schedules/mySchedule"));
    }

    @Test
    void shouldReturnStudentScheduleView() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenThrow(IllegalArgumentException.class);
        when(studentService.getStudentById(userDetails.getUser().getId())).thenReturn(new Student(1L, "user", "user","email@example.com"));
        this.mockMvc
                .perform(get("/mySchedule").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("schedules/mySchedule"));
    }

    @Test
    void shouldRedirectToScheduleWhenUserNotStudentOrTeacher() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenThrow(IllegalArgumentException.class);
        when(studentService.getStudentById(userDetails.getUser().getId())).thenThrow(IllegalArgumentException.class);
        this.mockMvc
                .perform(get("/mySchedule").with(user(userDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/"))
                .andExpect(flash().attributeExists("error"));

    }

    @Test
    void shouldReturnTeacherScheduleDataWithParams() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        List<CalendarDTO> calendar = new ArrayList<>(Collections.singletonList(new CalendarDTO("test", "2022-12-10 14:12:00", "2022-12-10 13:12:00", "test[PRACTICE]<br>Aud. test<br>teacher test<br>test")));
        List<Lesson> lessons = Collections.singletonList(
                new Lesson(1L,
                        new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")),
                        new Teacher(1L, "test", "123","email@example.com"),
                        LocalDate.of(2022, 12, 10),
                        new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)),
                        new Audience(1L, "test"),
                        TypeOfLesson.PRACTICE));
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenReturn(new Teacher(1L, "user", "user","email@example.com"));
        when(lessonService.getTeacherLessons(new Teacher(1L, "user", "user","email@example.com"), LocalDate.parse("2022-11-04"), LocalDate.parse("2022-11-04"))).thenReturn(lessons);
        when(lessonMapper.toCalendarDTO(lessons.get(0))).thenReturn(calendar.get(0));

        MvcResult result = this.mockMvc
                .perform(get("/mySchedule/data").with(user(userDetails))
                        .param("startDate", "2022-11-04")
                        .param("endDate", "2022-11-04")
                )
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        String expectedJson = "[{\"title\":\"test\",\"start\":\"2022-12-10 14:12:00\",\"end\":\"2022-12-10 13:12:00\",\"description\":\"test[PRACTICE]<br>Aud. test<br>teacher test<br>test\"}]";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void shouldReturnTeacherScheduleDataWithoutParams() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        List<CalendarDTO> calendar = new ArrayList<>(Collections.singletonList(new CalendarDTO("test", "2022-12-10 14:12:00", "2022-12-10 13:12:00", "test[PRACTICE]<br>Aud. test<br>teacher test<br>test")));
        List<Lesson> lessons = Collections.singletonList(
                new Lesson(1L,
                        new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")),
                        new Teacher(1L, "test", "123","email@example.com"),
                        LocalDate.of(2022, 12, 10),
                        new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)),
                        new Audience(1L, "test"),
                        TypeOfLesson.PRACTICE));
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenReturn(new Teacher(1L, "user", "user","email@example.com"));
        when(lessonService.getTeacherLessons(new Teacher(1L, "user", "user","email@example.com"), LocalDate.now(), LocalDate.now().plusDays(7))).thenReturn(lessons);
        when(lessonMapper.toCalendarDTO(lessons.get(0))).thenReturn(calendar.get(0));

        MvcResult result = this.mockMvc
                .perform(get("/mySchedule/data").with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        String expectedJson = "[{\"title\":\"test\",\"start\":\"2022-12-10 14:12:00\",\"end\":\"2022-12-10 13:12:00\",\"description\":\"test[PRACTICE]<br>Aud. test<br>teacher test<br>test\"}]";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void shouldReturnStudentScheduleDataWithParams() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        Group group = new Group(1L, "test", Course.FIRST, new Specialty(1L, "test"));
        Student student = new Student(1L, "user", "user","email@example.com", group);
        List<CalendarDTO> calendar = new ArrayList<>(Collections.singletonList(new CalendarDTO("test", "2022-12-10 14:12:00", "2022-12-10 13:12:00", "test[PRACTICE]<br>Aud. test<br>teacher test<br>test")));
        List<Lesson> lessons = Collections.singletonList(
                new Lesson(1L,
                        new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")),
                        new Teacher(1L, "test", "123","email@example.com"),
                        LocalDate.of(2022, 12, 10),
                        new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)),
                        new Audience(1L, "test"),
                        TypeOfLesson.PRACTICE));
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenThrow(IllegalArgumentException.class);
        when(studentService.getStudentById(student.getId())).thenReturn(student);
        when(lessonService.getGroupLessons(student.getGroup(), LocalDate.parse("2022-11-04"), LocalDate.parse("2022-11-04"))).thenReturn(lessons);
        when(lessonMapper.toCalendarDTO(lessons.get(0))).thenReturn(calendar.get(0));

        MvcResult result = this.mockMvc
                .perform(get("/mySchedule/data").with(user(userDetails))
                        .param("startDate", "2022-11-04")
                        .param("endDate", "2022-11-04")
                )
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        String expectedJson = "[{\"title\":\"test\",\"start\":\"2022-12-10 14:12:00\",\"end\":\"2022-12-10 13:12:00\",\"description\":\"test[PRACTICE]<br>Aud. test<br>teacher test<br>test\"}]";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void shouldReturnStudentScheduleDataWithoutParams() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        Group group = new Group(1L, "test", Course.FIRST, new Specialty(1L, "test"));
        Student student = new Student(1L, "user", "user","email@example.com", group);
        List<CalendarDTO> calendar = new ArrayList<>(Collections.singletonList(new CalendarDTO("test", "2022-12-10 14:12:00", "2022-12-10 13:12:00", "test[PRACTICE]<br>Aud. test<br>teacher test<br>test")));
        List<Lesson> lessons = Collections.singletonList(
                new Lesson(1L,
                        new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")),
                        new Teacher(1L, "test", "123","email@example.com"),
                        LocalDate.of(2022, 12, 10),
                        new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)),
                        new Audience(1L, "test"),
                        TypeOfLesson.PRACTICE));
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenThrow(IllegalArgumentException.class);
        when(studentService.getStudentById(student.getId())).thenReturn(student);
        when(lessonService.getGroupLessons(student.getGroup(), LocalDate.now(), LocalDate.now().plusDays(7))).thenReturn(lessons);
        when(lessonMapper.toCalendarDTO(lessons.get(0))).thenReturn(calendar.get(0));

        MvcResult result = this.mockMvc
                .perform(get("/mySchedule/data").with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        String expectedJson = "[{\"title\":\"test\",\"start\":\"2022-12-10 14:12:00\",\"end\":\"2022-12-10 13:12:00\",\"description\":\"test[PRACTICE]<br>Aud. test<br>teacher test<br>test\"}]";
        assertEquals(expectedJson, actualJson);
    }

}
