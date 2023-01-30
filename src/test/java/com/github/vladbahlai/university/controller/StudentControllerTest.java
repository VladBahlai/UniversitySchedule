package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.model.Student;
import com.github.vladbahlai.university.service.GroupService;
import com.github.vladbahlai.university.service.RoleService;
import com.github.vladbahlai.university.service.StudentService;
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

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @MockBean
    StudentService studentService;
    @MockBean
    GroupService groupService;
    @MockBean
    RoleService roleService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test", authorities = "READ_STUDENT")
    void shouldReturnViewWithStudentPage() throws Exception {
        List<Student> studentList = Arrays.asList(new Student(1L, "test", "123","email@example.com"), new Student(2L, "test", "123","email@example.com"));
        Page<Student> students = new PageImpl<>(studentList);
        when(studentService.getPage(PageRequest.of(0, 25))).thenReturn(students);
        this.mockMvc
                .perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/index"))
                .andExpect(model().attribute("students", students))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));

    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_STUDENT")
    void shouldReturnStudentCreateForm() throws Exception {
        List<Group> groups = Arrays.asList(
                new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")),
                new Group(2L, "test", Course.FIRST, new Specialty(1L, "test")));
        when(groupService.getGroups()).thenReturn(groups);
        this.mockMvc
                .perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/new"))
                .andExpect(model().attribute("student", new Student()))
                .andExpect(model().attribute("groups", groups));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_STUDENT")
    void shouldCreateStudent() throws Exception {
        Student student = new Student("test", "123","email@example.com", new Group("test", Course.FIRST));
        this.mockMvc
                .perform(post("/students")
                        .flashAttr("student", student).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/students"))
                .andExpect(flash().attributeExists("success"));

        verify(studentService, times(1)).saveStudent(student);
        verify(roleService, times(1)).getRoleByName("ROLE_STUDENT");
    }

    @Test
    @WithMockUser(username = "test", authorities = "DELETE_STUDENT")
    void shouldDeleteStudent() throws Exception {
        this.mockMvc
                .perform(delete("/students/{id}", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/students"))
                .andExpect(flash().attributeExists("success"));

        verify(studentService, times(1)).deleteStudent(1L);

    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_STUDENT")
    void shouldReturnStudentEditForm() throws Exception {
        List<Group> groups = Arrays.asList(
                new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")),
                new Group(2L, "test", Course.FIRST, new Specialty(1L, "test")));
        Student student = new Student("test", "123","email@example.com", groups.get(0));

        when(groupService.getGroups()).thenReturn(groups);
        when(studentService.getStudentById(1L)).thenReturn(student);
        this.mockMvc
                .perform(get("/students/{id}/edit", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/edit"))
                .andExpect(model().attribute("student", student))
                .andExpect(model().attribute("groups", groups));

    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_STUDENT")
    void shouldUpdateStudent() throws Exception {
        Student student = new Student(1L, "test", "123","email@example.com", new Group("test", Course.FIRST));
        when(studentService.getStudentById(student.getId())).thenReturn(student);
        this.mockMvc
                .perform(patch("/students/{id}", "1")
                        .flashAttr("student", student).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/students"))
                .andExpect(flash().attributeExists("success"));

        verify(studentService, times(1)).saveStudent(student);

    }

}
