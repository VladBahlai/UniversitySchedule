package com.github.vladbahlai.university.controller;


import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.mapper.SpecialtyMapper;
import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.service.DepartmentService;
import com.github.vladbahlai.university.service.DisciplineService;
import com.github.vladbahlai.university.service.SpecialtyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudyPlanController.class)
class StudyPlanControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    DepartmentService departmentService;
    @MockBean
    DisciplineService disciplineService;
    @MockBean
    SpecialtyService specialtyService;
    @MockBean
    SpecialtyMapper specialtyMapper;

    @Test
    @WithMockUser(username = "test", authorities = "READ")
    void shouldReturnSUdyPlanViewWithoutParams() throws Exception {
        List<Department> departments = Arrays.asList(new Department("test"), new Department("test"));
        when(departmentService.getDepartments()).thenReturn(departments);
        this.mockMvc
                .perform(get("/studyPlan"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("departments", departments))
                .andExpect(model().attribute("courses", Course.values()));
    }

    @Test
    @WithMockUser(username = "test", authorities = "READ")
    void shouldReturnSUdyPlanViewWithParams() throws Exception {
        List<Department> departments = Arrays.asList(new Department("test"), new Department("test"));
        Specialty specialty = new Specialty(1L, "test");
        List<Discipline> disciplines = Collections.singletonList(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test", new Department("test"))));

        when(departmentService.getDepartments()).thenReturn(departments);
        when(specialtyService.getSpecialtyById(1L)).thenReturn(specialty);
        when(disciplineService.getAllByCourseAndSpecialty(Course.FIRST, specialty)).thenReturn(disciplines);
        this.mockMvc
                .perform(get("/studyPlan")
                        .param("specialtyId", "1")
                        .param("course", "FIRST"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("departments", departments))
                .andExpect(model().attribute("courses", Course.values()))
                .andExpect(model().attribute("disciplines", disciplines));
    }

}
