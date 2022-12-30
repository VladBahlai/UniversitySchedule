package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.enums.TypeOfLesson;
import com.github.vladbahlai.university.mapper.CalendarMapper;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.service.GroupService;
import com.github.vladbahlai.university.service.LessonService;
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

@WebMvcTest(GroupScheduleController.class)
class GroupScheduleControllerTest {

    @MockBean
    LessonService lessonService;
    @MockBean
    CalendarMapper lessonMapper;
    @MockBean
    GroupService groupService;
    @Autowired
    MockMvc mockMvc;


    @Test
    @WithMockUser(username = "test", authorities = "READ_LESSON")
    void shouldReturnGroupScheduleView() throws Exception {
        List<Group> groups = Arrays.asList(
                new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")),
                new Group(2L, "test", Course.FIRST, new Specialty(1L, "test")));
        when(groupService.getGroups()).thenReturn(groups);
        this.mockMvc
                .perform(get("/schedule"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("groups", groups))
                .andExpect(view().name("schedules/schedule"));
    }

    @Test
    @WithMockUser(username = "test", authorities = "READ_LESSON")
    void shouldReturnGroupScheduleData() throws Exception {
        Group group = new Group(1L, "test", Course.FIRST, new Specialty(1L, "test"));
        List<CalendarDTO> calendar = new ArrayList<>(Collections.singletonList(new CalendarDTO("test", "2022-12-10 14:12:00", "2022-12-10 13:12:00", "test[PRACTICE]<br>Aud. test<br>teacher test<br>test")));
        List<Lesson> lessons = Collections.singletonList(
                new Lesson(1L,
                        new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")), group,
                        new Teacher(1L, "test", "123"),
                        LocalDate.of(2022, 12, 10),
                        new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)),
                        new Audience(1L, "test"), TypeOfLesson.PRACTICE));
        when(groupService.getGroupById(1L)).thenReturn(group);
        when(lessonService.getGroupLessons(group, LocalDate.parse("2022-11-04"), LocalDate.parse("2022-11-04"))).thenReturn(lessons);
        when(lessonMapper.toCalendarDTO(lessons.get(0))).thenReturn(calendar.get(0));
        MvcResult result = this.mockMvc
                .perform(get("/schedule/data")
                        .param("startDate", "2022-11-04")
                        .param("endDate", "2022-11-04")
                        .param("groupId", "1")
                )
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        String expectedJson = "[{\"title\":\"test\",\"start\":\"2022-12-10 14:12:00\",\"end\":\"2022-12-10 13:12:00\",\"description\":\"test[PRACTICE]<br>Aud. test<br>teacher test<br>test\"}]";
        assertEquals(expectedJson, actualJson);
    }

}
