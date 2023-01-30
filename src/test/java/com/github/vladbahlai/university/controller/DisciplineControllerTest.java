package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.security.MyUserDetails;
import com.github.vladbahlai.university.service.DisciplineService;
import com.github.vladbahlai.university.service.SpecialtyService;
import com.github.vladbahlai.university.service.StudentService;
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
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DisciplineController.class)
class DisciplineControllerTest {

    @MockBean
    DisciplineService disciplineService;
    @MockBean
    SpecialtyService specialtyService;
    @MockBean
    TeacherService teacherService;
    @MockBean
    StudentService studentService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test", authorities = "READ_DISCIPLINE")
    void shouldReturnViewWithDisciplinePage() throws Exception {
        List<Discipline> disciplineList = Collections.singletonList(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")));
        Page<Discipline> disciplines = new PageImpl<>(disciplineList);
        when(disciplineService.getPage(PageRequest.of(0, 25))).thenReturn(disciplines);
        this.mockMvc
                .perform(get("/disciplines"))
                .andExpect(status().isOk())
                .andExpect(view().name("disciplines/index"))
                .andExpect(model().attribute("disciplines", disciplines))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_DISCIPLINE")
    void shouldReturnDisciplineCreateForm() throws Exception {
        List<Specialty> specialties = Arrays.asList(new Specialty(1L, "test"), new Specialty(2L, "test"));
        when(specialtyService.getSpecialties()).thenReturn(specialties);
        this.mockMvc
                .perform(get("/disciplines/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("disciplines/new"))
                .andExpect(model().attribute("discipline", new Discipline()))
                .andExpect(model().attribute("specialties", specialties));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_DISCIPLINE")
    void shouldCreateDiscipline() throws Exception {
        Discipline discipline = new Discipline("name", 3.0, 120, Course.FIRST, new Specialty(1L, "name"));
        this.mockMvc
                .perform(post("/disciplines")
                        .flashAttr("discipline", discipline).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/disciplines"))
                .andExpect(flash().attributeExists("success"));
        verify(disciplineService, times(1)).saveDiscipline(discipline);
    }

    @Test
    @WithMockUser(username = "test", authorities = "DELETE_DISCIPLINE")
    void shouldDeleteDiscipline() throws Exception {
        this.mockMvc
                .perform(delete("/disciplines/{id}", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/disciplines"))
                .andExpect(flash().attributeExists("success"));
        verify(disciplineService, times(1)).deleteDiscipline(1L);
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_DISCIPLINE")
    void shouldReturnDisciplineEditForm() throws Exception {
        List<Specialty> specialties = Arrays.asList(new Specialty(1L, "test"), new Specialty(2L, "test"));
        Discipline discipline = new Discipline(1L, "name", 3.0, 120, Course.FIRST, specialties.get(0));

        when(specialtyService.getSpecialties()).thenReturn(specialties);
        when(disciplineService.getDisciplineById(1L)).thenReturn(discipline);
        this.mockMvc
                .perform(get("/disciplines/{id}/edit", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("disciplines/edit"))
                .andExpect(model().attribute("discipline", discipline))
                .andExpect(model().attribute("specialties", specialties));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_DISCIPLINE")
    void shouldUpdateDiscipline() throws Exception {
        Discipline discipline = new Discipline(1L, "name", 3.0, 120, Course.FIRST, new Specialty(1L, "name"));
        this.mockMvc
                .perform(patch("/disciplines/{id}", "1")
                        .flashAttr("discipline", discipline).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/disciplines"))
                .andExpect(flash().attributeExists("success"));

        verify(disciplineService, times(1)).saveDiscipline(discipline);
    }

    @Test
    void shouldReturnDisciplinesOfTeacher() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        Teacher teacher = new Teacher(1L, "user", "user","email@example.com");
        Discipline firstDiscipline = new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test"));
        Discipline secondDiscipline = new Discipline(3L, "test", 4.0, 150, Course.FIRST, new Specialty(1L, "test"));
        teacher.getDisciplines().add(firstDiscipline);
        teacher.getDisciplines().add(secondDiscipline);

        when(studentService.getStudentById(userDetails.getUser().getId())).thenThrow(IllegalArgumentException.class);
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenReturn(teacher);
        this.mockMvc
                .perform(get("/disciplines/my").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("disciplines", teacher.getDisciplines()));

    }

    @Test
    void shouldReturnDisciplinesOfStudent() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        Student student = new Student(1L, "user", "user","email@example.com", new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")));
        List<Discipline> disciplines = Arrays.asList(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")),
                new Discipline(3L, "test", 4.0, 150, Course.FIRST, new Specialty(1L, "test")));

        when(studentService.getStudentById(userDetails.getUser().getId())).thenReturn(student);
        when(disciplineService.getAllByCourseAndSpecialty(student.getGroup().getCourse(), student.getGroup().getSpecialty())).thenReturn(disciplines);
        this.mockMvc
                .perform(get("/disciplines/my").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("disciplines", disciplines));

    }

    @Test
    void shouldRedirectToHomeIfUserStudentOrTeacher() throws Exception {
        MyUserDetails userDetails = new MyUserDetails(new User(1L, "user", "user","email@example.com"), new HashSet<>(Collections.singletonList(new Privilege("READ_LESSON"))));
        when(studentService.getStudentById(userDetails.getUser().getId())).thenThrow(IllegalArgumentException.class);
        when(teacherService.getTeacherById(userDetails.getUser().getId())).thenThrow(IllegalArgumentException.class);
        this.mockMvc
                .perform(get("/disciplines/my").with(user(userDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "You don't have any disciplines. If you student or teacher tell about this to staff."))
                .andExpect(header().string("Location", "/"));

    }
}
