package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.dto.TeacherDTO;
import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.enums.TypeOfLesson;
import com.github.vladbahlai.university.mapper.CalendarMapper;
import com.github.vladbahlai.university.mapper.TeacherMapper;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.service.DepartmentService;
import com.github.vladbahlai.university.service.LessonService;
import com.github.vladbahlai.university.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherScheduleController.class)
public class TeacherScheduleControllerTest {

    @MockBean
    DepartmentService departmentService;
    @MockBean
    TeacherService teacherService;
    @MockBean
    LessonService lessonService;
    @MockBean
    CalendarMapper calendarMapper;
    @MockBean
    TeacherMapper teacherMapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test")
    void shouldReturnTeacherScheduleView() throws Exception {
        List<Department> departments = Arrays.asList(new Department("test"), new Department("test"));
        when(departmentService.getDepartments()).thenReturn(departments);
        this.mockMvc
                .perform(get("/teacherSchedule"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("departments", departments))
                .andExpect(view().name("schedules/teacherSchedule"));
    }

    @Test
    @WithMockUser(username = "test")
    void shouldReturnTeachersDTO() throws Exception {
        Department department = new Department(1L, "test");
        List<Teacher> teacherList = Arrays.asList(
                new Teacher(1L, "test", "123", department),
                new Teacher(2L, "test", "123", department));
        department.getTeachers().addAll(teacherList);
        List<TeacherDTO> teacherDTOList = Arrays.asList(
                new TeacherDTO("1", "test", "1", "test"),
                new TeacherDTO("2", "test", "1", "test")
        );
        when(departmentService.getDepartmentById(1L)).thenReturn(department);
        when(teacherMapper.toTeacherDTO(teacherList.get(0))).thenReturn(teacherDTOList.get(0));
        when(teacherMapper.toTeacherDTO(teacherList.get(1))).thenReturn(teacherDTOList.get(1));

        MvcResult actual = this.mockMvc
                .perform(get("/teacherSchedule/teachersByDepartment")
                        .param("departmentId", "1"))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = actual.getResponse().getContentAsString();
        String expectedJson = "[{\"id\":\"1\",\"name\":\"test\",\"departmentId\":\"1\",\"departmentName\":\"test\"},{\"id\":\"2\",\"name\":\"test\",\"departmentId\":\"1\",\"departmentName\":\"test\"}]";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    @WithMockUser(username = "test")
    void shouldReturnTeacherScheduleData() throws Exception {
        Teacher teacher = new Teacher(1L, "test", "123");
        String date = "2022-11-04";
        List<CalendarDTO> calendar = new ArrayList<>(Collections.singletonList(new CalendarDTO("test", "2022-12-10 14:12:00", "2022-12-10 13:12:00", "test[PRACTICE]<br>Aud. test<br>teacher test<br>test")));
        List<Lesson> lessons = Collections.singletonList(
                new Lesson(1L,
                        new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")), new Teacher(1L, "test", "123"),
                        LocalDate.of(2022, 12, 10), new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)),
                        new Audience(1L, "test"), TypeOfLesson.PRACTICE));
        when(teacherService.getTeacherById(teacher.getId())).thenReturn(teacher);
        when(lessonService.getTeacherLessons(teacher, LocalDate.parse(date), LocalDate.parse(date))).thenReturn(lessons);
        when(calendarMapper.toCalendarDTO(lessons.get(0))).thenReturn(calendar.get(0));
        MvcResult actual = this.mockMvc
                .perform(get("/teacherSchedule/data")
                        .param("startDate", date)
                        .param("endDate", date)
                        .param("teacherId", "1"))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = actual.getResponse().getContentAsString();
        String expectedJson = "[{\"title\":\"test\",\"start\":\"2022-12-10 14:12:00\",\"end\":\"2022-12-10 13:12:00\",\"description\":\"test[PRACTICE]<br>Aud. test<br>teacher test<br>test\"}]";
        assertEquals(expectedJson, actualJson);
    }

}
