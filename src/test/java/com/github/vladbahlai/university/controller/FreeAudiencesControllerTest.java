package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.model.TimeSpan;
import com.github.vladbahlai.university.service.AudienceService;
import com.github.vladbahlai.university.service.TimeSpanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FreeAudiencesController.class)
class FreeAudiencesControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    TimeSpanService timeSpanService;
    @MockBean
    AudienceService audienceService;

    @Test
    @WithMockUser(username = "test", authorities = "READ_AUDIENCE")
    void shouldReturnFreeAudiencesViewWithoutParams() throws Exception {
        List<TimeSpan> timeSpans = Arrays.asList(
                new TimeSpan(1L, 1, LocalTime.of(14, 10), LocalTime.of(14, 12)),
                new TimeSpan(2L, 1, LocalTime.of(14, 10), LocalTime.of(14, 12)));
        when(timeSpanService.getTimeSpans()).thenReturn(timeSpans);
        this.mockMvc
                .perform(get("/freeAudiences"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/free"))
                .andExpect(model().attribute("timeSpans", timeSpans));
    }

    @Test
    @WithMockUser(username = "test", authorities = "READ_AUDIENCE")
    void shouldReturnFreeAudiencesViewWithParams() throws Exception {
        List<TimeSpan> timeSpans = Arrays.asList(
                new TimeSpan(1L, 1, LocalTime.of(14, 10), LocalTime.of(14, 12)),
                new TimeSpan(2L, 1, LocalTime.of(14, 10), LocalTime.of(14, 12)));
        List<Audience> audiences = Arrays.asList(new Audience("test"), new Audience("test"));

        when(timeSpanService.getTimeSpans()).thenReturn(timeSpans);
        when(timeSpanService.getTimeSpanById(1L)).thenReturn(timeSpans.get(0));
        when(audienceService.getFreeAudiences(LocalDate.of(2022, 11, 21), timeSpans.get(0))).thenReturn(audiences);
        this.mockMvc
                .perform(get("/freeAudiences")
                        .param("date", "2022-11-21")
                        .param("timeSpanId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/free"))
                .andExpect(model().attribute("timeSpans", timeSpans))
                .andExpect(model().attribute("audiences", audiences));
    }
}
