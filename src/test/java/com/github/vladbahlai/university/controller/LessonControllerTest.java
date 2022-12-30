package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.enums.TypeOfLesson;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
class LessonControllerTest {

    @MockBean
    LessonService lessonService;
    @MockBean
    GroupService groupService;
    @MockBean
    TeacherService teacherService;
    @MockBean
    TimeSpanService timeSpanService;
    @MockBean
    AudienceService audienceService;
    @MockBean
    DisciplineService disciplineService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_LESSON")
    void shouldReturnViewWithLessonPage() throws Exception {
        Discipline discipline = new Discipline(1L, "name", 3.0, 120, Course.FIRST, new Specialty(1L, "test"));
        TimeSpan timeSpan = new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12));
        Teacher teacher = new Teacher(1L, "test", "123");
        Audience audience = new Audience(1L, "name");
        Group group = new Group("GZ-12", Course.FIRST, new Specialty(1L, "test"));

        List<Lesson> lessonList = Arrays.asList(
                new Lesson(1L, discipline, group, teacher, LocalDate.of(2022, 12, 10), timeSpan, audience, TypeOfLesson.PRACTICE),
                new Lesson(2L, discipline, group, teacher, LocalDate.of(2022, 12, 12), timeSpan, audience, TypeOfLesson.PRACTICE));
        Page<Lesson> lessons = new PageImpl<>(lessonList);
        when(lessonService.getPage(PageRequest.of(0, 25))).thenReturn(lessons);
        this.mockMvc
                .perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(view().name("lessons/index"))
                .andExpect(model().attribute("lessons", lessons))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_LESSON")
    void shouldReturnLessonCreateForm() throws Exception {
        List<Discipline> disciplines = Collections.singletonList(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")));
        List<Group> groups = Collections.singletonList(
                new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")));
        List<TimeSpan> timeSpans = Collections.singletonList(
                new TimeSpan(1L, 1, LocalTime.of(14, 10), LocalTime.of(14, 12)));
        List<Teacher> teachers = Collections.singletonList(
                new Teacher(1L, "test", "123"));
        List<Audience> audiences = Collections.singletonList(new Audience(1L, "test"));

        when(audienceService.getAudiences()).thenReturn(audiences);
        when(teacherService.getTeachers()).thenReturn(teachers);
        when(timeSpanService.getTimeSpans()).thenReturn(timeSpans);
        when(disciplineService.getDisciplines()).thenReturn(disciplines);
        when(groupService.getGroups()).thenReturn(groups);

        this.mockMvc
                .perform(get("/lessons/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("lessons/new"))
                .andExpect(model().attribute("lesson", new Lesson()))
                .andExpect(model().attribute("disciplines", disciplines))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("timeSpans", timeSpans))
                .andExpect(model().attribute("audiences", audiences));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_LESSON")
    void shouldCreateLesson() throws Exception {
        Discipline discipline = new Discipline(1L, "name", 3.0, 120, Course.FIRST, new Specialty(1L, "test"));
        TimeSpan timeSpan = new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12));
        Teacher teacher = new Teacher(1L, "test", "123");
        Audience audience = new Audience(1L, "name");
        Group group = new Group("GZ-12", Course.FIRST, new Specialty(1L, "test"));
        Lesson lesson = new Lesson(discipline, group, teacher, LocalDate.of(2022, 12, 10), timeSpan, audience, TypeOfLesson.PRACTICE);
        this.mockMvc
                .perform(post("/lessons")
                        .flashAttr("lesson", lesson).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/lessons"))
                .andExpect(flash().attributeExists("success"));

        verify(lessonService, times(1)).saveLesson(lesson);
    }

    @Test
    @WithMockUser(username = "test", authorities = "DELETE_LESSON")
    void shouldDeleteLesson() throws Exception {
        this.mockMvc
                .perform(delete("/lessons/{id}", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/lessons"))
                .andExpect(flash().attributeExists("success"));

        verify(lessonService, times(1)).deleteLesson(1L);
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_LESSON")
    void shouldReturnLessonEditForm() throws Exception {
        List<Discipline> disciplines = Collections.singletonList(
                new Discipline(1L, "test", 3.0, 120, Course.FIRST, new Specialty(1L, "test")));
        List<Group> groups = Collections.singletonList(
                new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")));
        List<TimeSpan> timeSpans = Collections.singletonList(
                new TimeSpan(1L, 1, LocalTime.of(14, 10), LocalTime.of(14, 12)));
        List<Teacher> teachers = Collections.singletonList(
                new Teacher(1L, "test", "123"));
        List<Audience> audiences = Collections.singletonList(new Audience(1L, "test"));
        Lesson lesson = new Lesson(1L,
                disciplines.get(0), groups.get(0), teachers.get(0), LocalDate.of(2022, 12, 10),
                timeSpans.get(0), audiences.get(0), TypeOfLesson.PRACTICE);
        when(audienceService.getAudiences()).thenReturn(audiences);
        when(teacherService.getTeachers()).thenReturn(teachers);
        when(timeSpanService.getTimeSpans()).thenReturn(timeSpans);
        when(disciplineService.getDisciplines()).thenReturn(disciplines);
        when(groupService.getGroups()).thenReturn(groups);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        this.mockMvc
                .perform(get("/lessons/{id}/edit", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("lessons/edit"))
                .andExpect(model().attribute("lesson", lesson))
                .andExpect(model().attribute("disciplines", disciplines))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("timeSpans", timeSpans))
                .andExpect(model().attribute("audiences", audiences));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_LESSON")
    void shouldUpdateLesson() throws Exception {
        Discipline discipline = new Discipline(1L, "name", 3.0, 120, Course.FIRST, new Specialty(1L, "test"));
        TimeSpan timeSpan = new TimeSpan(1L, 1, LocalTime.of(14, 12), LocalTime.of(13, 12));
        Teacher teacher = new Teacher(1L, "test", "123");
        Audience audience = new Audience(1L, "name");
        Group group = new Group("GZ-12", Course.FIRST, new Specialty(1L, "test"));
        Lesson lesson = new Lesson(1L, discipline, group, teacher, LocalDate.of(2022, 12, 10), timeSpan, audience, TypeOfLesson.PRACTICE);
        this.mockMvc
                .perform(patch("/lessons/{id}", "1")
                        .flashAttr("lesson", lesson).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/lessons"))
                .andExpect(flash().attributeExists("success"));

        verify(lessonService, times(1)).saveLesson(lesson);
    }
}
