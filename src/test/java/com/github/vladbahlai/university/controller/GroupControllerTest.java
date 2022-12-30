package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.service.GroupService;
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

@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @MockBean
    GroupService groupService;
    @MockBean
    SpecialtyService specialtyService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test", authorities = "READ_GROUP")
    void shouldReturnViewWithGroupPage() throws Exception {
        List<Group> groupList = Arrays.asList(
                new Group(1L, "test", Course.FIRST, new Specialty(1L, "test")),
                new Group(2L, "test", Course.FIRST, new Specialty(1L, "test")));
        Page<Group> groups = new PageImpl<>(groupList);
        when(groupService.getPage(PageRequest.of(0, 25))).thenReturn(groups);
        this.mockMvc
                .perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/index"))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_GROUP")
    void shouldReturnGroupCreateForm() throws Exception {
        List<Specialty> specialties = Arrays.asList(new Specialty(1L, "test"), new Specialty(2L, "test"));
        when(specialtyService.getSpecialties()).thenReturn(specialties);
        this.mockMvc
                .perform(get("/groups/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/new"))
                .andExpect(model().attribute("group", new Group()))
                .andExpect(model().attribute("specialties", specialties));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_GROUP")
    void shouldCreateGroup() throws Exception {
        Group group = new Group("GZ-12", Course.FIRST, new Specialty(1L, "test"));
        this.mockMvc
                .perform(post("/groups")
                        .flashAttr("group", group).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/groups"));
        verify(groupService, times(1)).saveGroup(group);
    }

    @Test
    @WithMockUser(username = "test", authorities = "DELETE_GROUP")
    void shouldDeleteGroup() throws Exception {
        this.mockMvc
                .perform(delete("/groups/{id}", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/groups"));
        verify(groupService, times(1)).deleteGroup(1L);
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_GROUP")
    void shouldReturnGroupEditForm() throws Exception {
        List<Specialty> specialties = Arrays.asList(new Specialty(1L, "test"), new Specialty(2L, "test"));
        Group group = new Group("GZ-12", Course.FIRST, specialties.get(0));

        when(groupService.getGroupById(1L)).thenReturn(group);
        when(specialtyService.getSpecialties()).thenReturn(specialties);
        this.mockMvc
                .perform(get("/groups/{id}/edit", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/edit"))
                .andExpect(model().attribute("group", group))
                .andExpect(model().attribute("specialties", specialties));
    }

    @Test
    @WithMockUser(username = "test", authorities = "WRITE_GROUP")
    void shouldUpdateGroup() throws Exception {
        Group group = new Group(1L, "GZ-12", Course.FIRST, new Specialty(1L, "test"));
        this.mockMvc
                .perform(patch("/groups/{id}", "1")
                        .flashAttr("group", group).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/groups"));
        verify(groupService, times(1)).saveGroup(group);
    }
}
