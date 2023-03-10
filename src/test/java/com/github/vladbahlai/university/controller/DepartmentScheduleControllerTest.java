package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.enums.TypeOfLesson;
import com.github.vladbahlai.university.mapper.CalendarMapper;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.security.MyUserDetails;
import com.github.vladbahlai.university.service.LessonService;
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

@WebMvcTest(DepartmentScheduleController.class)
class DepartmentScheduleControllerTest {

    @MockBean
    TeacherService teacherService;
    @MockBean
    LessonService lessonService;
    @MockBean
    CalendarMapper lessonMapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnDepartmentScheduleView() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        Department department = new Department(1L, "test");
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenReturn(new Teacher(1L, "user", "user","email@example.com", department));
        this.mockMvc
                .perform(get("/departmentSchedule").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("schedules/departmentSchedule"))
                .andExpect(model().attribute("department", department));
    }

    @Test
    void shouldReturnDepartmentScheduleDataWithoutParams() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        Department department = new Department(1L, "test");
        Teacher teacher = new Teacher(1L, "user", "user","email@example.com", department);
        List<CalendarDTO> calendar = new ArrayList<>(Collections.singletonList(new CalendarDTO("test", "2022-12-10 14:12:00", "2022-12-10 13:12:00", "test[PRACTICE]<br>Aud. test<br>teacher test<br>test")));
        List<Lesson> lessons = Collections.singletonList(
                new Lesson(1L,
                        new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")), new Teacher(1L, "test", "123","email@example.com"),
                        LocalDate.of(2022, 12, 10), new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)),
                        new Audience(1L, "test"), TypeOfLesson.PRACTICE));
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenReturn(teacher);
        when(lessonService.getDepartmentLessons(teacher.getDepartment(), LocalDate.now(), LocalDate.now().plusDays(7))).thenReturn(lessons);
        when(lessonMapper.toCalendarDTO(lessons.get(0))).thenReturn(calendar.get(0));

        MvcResult result = this.mockMvc
                .perform(get("/departmentSchedule/data").with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        String expectedJson = "[{\"title\":\"test\",\"start\":\"2022-12-10 14:12:00\",\"end\":\"2022-12-10 13:12:00\",\"description\":\"test[PRACTICE]<br>Aud. test<br>teacher test<br>test\"}]";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void shouldReturnDepartmentScheduleDataWithParams() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        Department department = new Department(1L, "test");
        Teacher teacher = new Teacher(1L, "user", "user","email@example.com", department);
        List<CalendarDTO> calendar = new ArrayList<>(Collections.singletonList(new CalendarDTO("test", "2022-12-10 14:12:00", "2022-12-10 13:12:00", "test[PRACTICE]<br>Aud. test<br>teacher test<br>test")));
        List<Lesson> lessons = Collections.singletonList(
                new Lesson(1L,
                        new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")), new Teacher(1L, "test", "123","email@example.com"),
                        LocalDate.of(2022, 12, 10), new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)),
                        new Audience(1L, "test"), TypeOfLesson.PRACTICE));
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenReturn(teacher);
        when(lessonService.getDepartmentLessons(teacher.getDepartment(), LocalDate.parse("2022-11-04"), LocalDate.parse("2022-11-04"))).thenReturn(lessons);
        when(lessonMapper.toCalendarDTO(lessons.get(0))).thenReturn(calendar.get(0));

        MvcResult result = this.mockMvc
                .perform(get("/departmentSchedule/data").with(user(userDetails))
                        .param("startDate", "2022-11-04")
                        .param("endDate", "2022-11-04"))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        String expectedJson = "[{\"title\":\"test\",\"start\":\"2022-12-10 14:12:00\",\"end\":\"2022-12-10 13:12:00\",\"description\":\"test[PRACTICE]<br>Aud. test<br>teacher test<br>test\"}]";
        assertEquals(expectedJson, actualJson);
    }
}
