package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.model.Teacher;
import com.github.vladbahlai.university.service.DepartmentService;
import com.github.vladbahlai.university.service.DisciplineService;
import com.github.vladbahlai.university.service.RoleService;
import com.github.vladbahlai.university.service.TeacherService;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    @MockBean
    TeacherService teacherService;
    @MockBean
    DepartmentService departmentService;
    @MockBean
    DisciplineService disciplineService;
    @MockBean
    RoleService roleService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test", authorities = "READ_TEACHER")
    void shouldReturnViewWitTeacherPage() throws Exception {
        List<Teacher> teacherList = Arrays.asList(
                new Teacher(1L, "test", "123"),
                new Teacher(2L, "test", "123"));
        Page<Teacher> teachers = new PageImpl<>(teacherList);
        when(teacherService.getPage(PageRequest.of(0, 25))).thenReturn(teachers);
        this.mockMvc
                .perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/index"))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_TEACHER")
    void shouldReturnTeacherCreateForm() throws Exception {
        List<Department> departments = Arrays.asList(new Department("test"), new Department("test"));
        List<Discipline> disciplines = Collections.singletonList(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")));
        when(disciplineService.getDisciplines()).thenReturn(disciplines);
        when(departmentService.getDepartments()).thenReturn(departments);
        this.mockMvc
                .perform(get("/teachers/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/new"))
                .andExpect(model().attribute("teacher", new Teacher()))
                .andExpect(model().attribute("departments", departments))
                .andExpect(model().attribute("disciplines", disciplines));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_TEACHER")
    void shouldCreateTeacher() throws Exception {
        Teacher teacher = new Teacher("test", "123", new Department(1L, "test"));
        this.mockMvc
                .perform(post("/teachers")
                        .flashAttr("teacher", teacher).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/teachers"))
                .andExpect(flash().attributeExists("success"));

        verify(teacherService, times(1)).saveTeacher(teacher);
        verify(roleService, times(1)).getRoleByName("ROLE_TEACHER");
    }

    @Test
    @WithMockUser(username = "test", authorities = "DELETE_TEACHER")
    void shouldDeleteTeacher() throws Exception {
        this.mockMvc
                .perform(delete("/teachers/{id}", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/teachers"))
                .andExpect(flash().attributeExists("success"));

        verify(teacherService, times(1)).deleteTeacher(1L);
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_TEACHER")
    void shouldReturnTeacherEditForm() throws Exception {
        List<Department> departments = Arrays.asList(new Department("test"), new Department("test"));
        List<Discipline> disciplines = Collections.singletonList(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")));
        Teacher teacher = new Teacher(1L, "test", "123", departments.get(0));

        when(disciplineService.getDisciplines()).thenReturn(disciplines);
        when(departmentService.getDepartments()).thenReturn(departments);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);
        this.mockMvc
                .perform(get("/teachers/{id}/edit", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/edit"))
                .andExpect(model().attribute("teacher", teacher))
                .andExpect(model().attribute("disciplines", disciplines))
                .andExpect(model().attribute("departments", departments));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_TEACHER")
    void shouldUpdateTeacher() throws Exception {
        Teacher teacher = new Teacher(1L, "test", "123", new Department(1L, "test"));
        when(teacherService.getTeacherById(teacher.getId())).thenReturn(teacher);
        this.mockMvc
                .perform(patch("/teachers/{id}", 1)
                        .flashAttr("teacher", teacher).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/teachers"))
                .andExpect(flash().attributeExists("success"));

        verify(teacherService, times(1)).saveTeacher(teacher);
    }
}
