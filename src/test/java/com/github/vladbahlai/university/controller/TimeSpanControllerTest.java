package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.model.TimeSpan;
import com.github.vladbahlai.university.service.TimeSpanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimeSpanController.class)
class TimeSpanControllerTest {

    @MockBean
    TimeSpanService timeSpanService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test", authorities = "READ_TIMESPAN")
    void shouldReturnViewWithTimeSpanPage() throws Exception {
        List<TimeSpan> timeSpanList = Arrays.asList(
                new TimeSpan(1L, 1, LocalTime.of(14, 10), LocalTime.of(14, 12)),
                new TimeSpan(2L, 1, LocalTime.of(14, 10), LocalTime.of(14, 12)));
        Page<TimeSpan> timeSpans = new PageImpl<>(timeSpanList);
        when(timeSpanService.getPage(PageRequest.of(0, 25))).thenReturn(timeSpans);
        this.mockMvc
                .perform(get("/timeSpans"))
                .andExpect(status().isOk())
                .andExpect(view().name("timeSpans/index"))
                .andExpect(model().attribute("timeSpans", timeSpans))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_TIMESPAN")
    void shouldReturnTimeSpanCreateForm() throws Exception {
        this.mockMvc
                .perform(get("/timeSpans/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("timeSpans/new"))
                .andExpect(model().attribute("timeSpan", new TimeSpan()));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_TIMESPAN")
    void shouldCreateTimeSpan() throws Exception {
        TimeSpan timeSpan = new TimeSpan(1, LocalTime.of(14, 10), LocalTime.of(14, 12));
        this.mockMvc
                .perform(post("/timeSpans")
                        .flashAttr("timeSpan", timeSpan).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/timeSpans"))
                .andExpect(flash().attributeExists("success"));

        verify(timeSpanService, times(1))
                .saveTimeSpan(timeSpan);
    }

    @Test
    @WithMockUser(username = "test", authorities = "DELETE_TIMESPAN")
    void shouldDeleteTimeSpan() throws Exception {
        this.mockMvc
                .perform(delete("/timeSpans/{id}", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/timeSpans"))
                .andExpect(flash().attributeExists("success"));

        verify(timeSpanService, times(1)).deleteTimeSpan(1L);
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_TIMESPAN")
    void shouldReturnTimeSpanEditForm() throws Exception {
        TimeSpan timeSpan = new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12));
        when(timeSpanService.getTimeSpanById(1L)).thenReturn(timeSpan);
        this.mockMvc
                .perform(get("/timeSpans/{id}/edit", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("timeSpans/edit"))
                .andExpect(model().attribute("timeSpan", timeSpan));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_TIMESPAN")
    void shouldUpdateTimeSpan() throws Exception {
        TimeSpan timeSpan = new TimeSpan(1L, 1, LocalTime.of(14, 10), LocalTime.of(14, 12));
        this.mockMvc
                .perform(patch("/timeSpans/{id}", "1")
                        .flashAttr("timeSpan", timeSpan).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/timeSpans"))
                .andExpect(flash().attributeExists("success"));

        verify(timeSpanService, times(1)).saveTimeSpan(timeSpan);
    }
}
