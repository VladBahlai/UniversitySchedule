package com.github.vladbahlai.university.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test", authorities = "ADMIN")
    void shouldReturnAdminPanelView() throws Exception {
        this.mockMvc
                .perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/menu"));

    }
}
