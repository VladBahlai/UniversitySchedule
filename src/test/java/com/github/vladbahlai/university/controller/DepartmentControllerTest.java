package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.service.DepartmentService;
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

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @MockBean
    DepartmentService departmentService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test", authorities = "READ_DEPARTMENT")
    void shouldReturnViewWithDepartmentPage() throws Exception {
        List<Department> departmentList = Arrays.asList(new Department("test"), new Department("test"));
        Page<Department> departments = new PageImpl<>(departmentList);
        when(departmentService.getPage(PageRequest.of(0, 25))).thenReturn(departments);
        this.mockMvc
                .perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(view().name("departments/index"))
                .andExpect(model().attribute("departments", departments))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_DEPARTMENT")
    void shouldReturnDepartmentCreateForm() throws Exception {
        this.mockMvc
                .perform(get("/departments/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("departments/new"))
                .andExpect(model().attribute("department", new Department()));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_DEPARTMENT")
    void shouldCreateDepartment() throws Exception {
        this.mockMvc
                .perform(post("/departments")
                        .flashAttr("department", new Department("name")).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/departments"));
        verify(departmentService, times(1)).saveDepartment(new Department("name"));

    }

    @Test
    @WithMockUser(username = "test", authorities = "DELETE_DEPARTMENT")
    void shouldDeleteDepartment() throws Exception {
        this.mockMvc
                .perform(delete("/departments/{id}", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/departments"));
        verify(departmentService, times(1)).deleteDepartment(1L);
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_DEPARTMENT")
    void shouldReturnDepartmentEditForm() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(new Department(1L, "name"));
        this.mockMvc
                .perform(get("/departments/{id}/edit", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("departments/edit"))
                .andExpect(model().attribute("department", departmentService.getDepartmentById(1L)));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_DEPARTMENT")
    void shouldUpdateDepartment() throws Exception {
        this.mockMvc
                .perform(patch("/departments/{id}", "1")
                        .flashAttr("department", new Department(1L, "name")).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/departments"));
        verify(departmentService, times(1)).saveDepartment(new Department(1L, "name"));
    }
}
