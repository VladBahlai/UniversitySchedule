package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.enums.TypeOfLesson;
import com.github.vladbahlai.university.mapper.CalendarMapper;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.service.AudienceService;
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

@WebMvcTest(AudienceScheduleController.class)
class AudienceScheduleControllerTest {

    @MockBean
    LessonService lessonService;
    @MockBean
    AudienceService audienceService;
    @MockBean
    CalendarMapper calendarMapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test")
    void shouldReturnAudienceScheduleView() throws Exception {
        List<Audience> audiences = Arrays.asList(new Audience("test"), new Audience("test"));
        when(audienceService.getAudiences()).thenReturn(audiences);
        this.mockMvc
                .perform(get("/audienceSchedule"))
                .andExpect(status().isOk())
                .andExpect(view().name("schedules/audienceSchedule"))
                .andExpect(model().attribute("audiences", audiences));
    }

    @Test
    @WithMockUser(username = "test")
    void shouldReturnAudienceScheduleData() throws Exception {
        Audience audience = new Audience(1L, "test");
        String date = "2022-11-04";
        List<CalendarDTO> calendar = new ArrayList<>(Collections.singletonList(new CalendarDTO("test", "2022-12-10 14:12:00", "2022-12-10 13:12:00", "test[PRACTICE]<br>Aud. test<br>teacher test<br>test")));
        List<Lesson> lessons = Collections.singletonList(
                new Lesson(1L,
                        new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                        new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")), new Teacher(1L, "test", "123","email@example.com"),
                        LocalDate.of(2022, 12, 10), new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12)),
                        audience, TypeOfLesson.PRACTICE));
        when(audienceService.getAudienceById(audience.getId())).thenReturn(audience);
        when(lessonService.getAudienceLessons(audience, LocalDate.parse(date), LocalDate.parse(date))).thenReturn(lessons);
        when(calendarMapper.toCalendarDTO(lessons.get(0))).thenReturn(calendar.get(0));
        MvcResult actual = this.mockMvc
                .perform(get("/audienceSchedule/data")
                        .param("startDate", date)
                        .param("endDate", date)
                        .param("audienceId", "1"))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = actual.getResponse().getContentAsString();
        String expectedJson = "[{\"title\":\"test\",\"start\":\"2022-12-10 14:12:00\",\"end\":\"2022-12-10 13:12:00\",\"description\":\"test[PRACTICE]<br>Aud. test<br>teacher test<br>test\"}]";
        assertEquals(expectedJson, actualJson);

    }


}
