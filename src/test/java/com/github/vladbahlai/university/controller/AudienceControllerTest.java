package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.service.AudienceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AudienceController.class)
class AudienceControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AudienceService audienceService;

    @Test
    @WithMockUser(username = "test", authorities = "READ_AUDIENCE")
    void shouldReturnViewWithAudiencePage() throws Exception {
        List<Audience> audienceList = Arrays.asList(new Audience("test"), new Audience("test"));
        Page<Audience> audiences = new PageImpl<>(audienceList);
        when(audienceService.getPage(PageRequest.of(0, 25))).thenReturn(audiences);
        this.mockMvc
                .perform(get("/audiences"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/index"))
                .andExpect(model().attribute("audiences", audiences))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_AUDIENCE")
    void shouldReturnAudienceCreateForm() throws Exception {
        this.mockMvc
                .perform(get("/audiences/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/new"))
                .andExpect(model().attribute("audience", new Audience()));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_AUDIENCE")
    void shouldCreateAudience() throws Exception {
        this.mockMvc.perform(post("/audiences")
                        .flashAttr("audience", new Audience("test")).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/audiences"))
                .andExpect(flash().attributeExists("success"));
        verify(audienceService, times(1)).saveAudience(new Audience("test"));
    }

    @Test
    @WithMockUser(username = "test", authorities = "DELETE_AUDIENCE")
    void shouldDeleteAudience() throws Exception {
        this.mockMvc
                .perform(delete("/audiences/{id}", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/audiences"))
                .andExpect(flash().attributeExists("success"));
        verify(audienceService, times(1)).deleteAudience(1L);
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_AUDIENCE")
    void shouldReturnAudienceEditForm() throws Exception {
        Audience audience = new Audience(1L, "name");
        when(audienceService.getAudienceById(1L)).thenReturn(audience);
        this.mockMvc
                .perform(get("/audiences/{id}/edit", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/edit"))
                .andExpect(model().attribute("audience", audience));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_AUDIENCE")
    void shouldUpdateAudience() throws Exception {
        Audience audience = new Audience(1L, "test");
        this.mockMvc
                .perform(patch("/audiences/{id}", "1")
                        .flashAttr("audience", audience).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/audiences"))
                .andExpect(flash().attributeExists("success"));
        verify(audienceService, times(1)).saveAudience(audience);
    }
}
