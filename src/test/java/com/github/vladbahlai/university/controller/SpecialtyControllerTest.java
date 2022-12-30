package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.service.DepartmentService;
import com.github.vladbahlai.university.service.SpecialtyService;
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

@WebMvcTest(SpecialtyController.class)
class SpecialtyControllerTest {

    @MockBean
    SpecialtyService specialtyService;
    @MockBean
    DepartmentService departmentService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test", authorities = "READ_SPECIALTY")
    void shouldReturnViewWithDataSpecialtyPage() throws Exception {
        List<Specialty> specialtyList = Arrays.asList(new Specialty(1L, "test", new Department("test")), new Specialty(1L, "test", new Department("test")));
        Page<Specialty> specialties = new PageImpl<>(specialtyList);
        when(specialtyService.getPage(PageRequest.of(0, 25))).thenReturn(specialties);
        this.mockMvc
                .perform(get("/specialties"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialties/index"))
                .andExpect(model().attribute("specialties", specialties))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_SPECIALTY")
    void shouldReturnSpecialtyCreateForm() throws Exception {
        List<Department> departments = Arrays.asList(new Department(1L, "test"), new Department(2L, "test"));
        when(departmentService.getDepartments()).thenReturn(departments);
        this.mockMvc
                .perform(get("/specialties/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialties/new"))
                .andExpect(model().attribute("specialty", new Specialty()))
                .andExpect(model().attribute("departments", departments));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_SPECIALTY")
    void shouldCreateSpecialty() throws Exception {
        Specialty specialty = new Specialty("test", new Department(1L, "test"));
        this.mockMvc
                .perform(post("/specialties")
                        .flashAttr("specialty", specialty).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/specialties"))
                .andExpect(flash().attributeExists("success"));

        verify(specialtyService, times(1)).saveSpecialty(specialty);
    }

    @Test
    @WithMockUser(username = "test", authorities = "DELETE_SPECIALTY")
    void shouldDeleteSpecialty() throws Exception {
        this.mockMvc
                .perform(delete("/specialties/{id}", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/specialties"))
                .andExpect(flash().attributeExists("success"));

        verify(specialtyService, times(1)).deleteSpecialty(1L);
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_SPECIALTY")
    void shouldReturnSpecialtyEditForm() throws Exception {
        List<Department> departments = Arrays.asList(new Department(1L, "test"), new Department(2L, "test"));
        Specialty specialty = new Specialty(1L, "test", departments.get(0));

        when(specialtyService.getSpecialtyById(1L)).thenReturn(specialty);
        when(departmentService.getDepartments()).thenReturn(departments);
        this.mockMvc
                .perform(get("/specialties/{id}/edit", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialties/edit"))
                .andExpect(model().attribute("specialty", specialty))
                .andExpect(model().attribute("departments", departments));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_SPECIALTY")
    void shouldUpdateSpecialty() throws Exception {
        Specialty specialty = new Specialty(1L, "test", new Department(1L, "test"));
        this.mockMvc
                .perform(patch("/specialties/{id}", "1")
                        .flashAttr("specialty", specialty).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/specialties"))
                .andExpect(flash().attributeExists("success"));

        verify(specialtyService, times(1)).saveSpecialty(specialty);
    }
}
