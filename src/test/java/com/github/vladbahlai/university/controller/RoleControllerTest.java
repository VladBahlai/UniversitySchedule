package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.model.Privilege;
import com.github.vladbahlai.university.model.Role;
import com.github.vladbahlai.university.service.PrivilegeService;
import com.github.vladbahlai.university.service.RoleService;
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

@WebMvcTest(RoleController.class)
@WithMockUser(username = "test", authorities = "ADMIN")
class RoleControllerTest {

    @MockBean
    RoleService roleService;
    @MockBean
    PrivilegeService privilegeService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnViewWithAllRoles() throws Exception {
        List<Role> roleList = Arrays.asList(new Role(1L, "test"), new Role(2L, "test"));
        Page<Role> roles = new PageImpl<>(roleList);
        when(roleService.getPage(PageRequest.of(0, 25))).thenReturn(roles);
        this.mockMvc
                .perform(get("/admin/roles"))
                .andExpect(status().isOk())
                .andExpect(view().name("roles/index"))
                .andExpect(model().attribute("roles", roles))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));
    }

    @Test
    void shouldReturnRoleCreateForm() throws Exception {
        List<Privilege> privileges = Arrays.asList(new Privilege(1L, "test"), new Privilege(2L, "test"));
        when(privilegeService.getPrivileges()).thenReturn(privileges);
        this.mockMvc
                .perform(get("/admin/roles/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("roles/new"))
                .andExpect(model().attribute("role", new Role()))
                .andExpect(model().attribute("privileges", privileges));
    }

    @Test
    void shouldCreateRole() throws Exception {
        Role role = new Role(1L, "test");
        this.mockMvc
                .perform(post("/admin/roles")
                        .flashAttr("role", role).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/roles"))
                .andExpect(flash().attributeExists("success"));

        verify(roleService, times(1)).saveRole(role);
    }

    @Test
    void shouldDeleteRole() throws Exception {
        this.mockMvc
                .perform(delete("/admin/roles/{id}", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/roles"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void shouldReturnRoleEditForm() throws Exception {
        List<Privilege> privileges = Arrays.asList(new Privilege(1L, "test"), new Privilege(2L, "test"));
        Role role = new Role(2L, "test");

        when(privilegeService.getPrivileges()).thenReturn(privileges);
        when(roleService.getRoleById(2L)).thenReturn(role);
        this.mockMvc
                .perform(get("/admin/roles/{id}/edit", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("roles/edit"))
                .andExpect(model().attribute("role", role))
                .andExpect(model().attribute("privileges", privileges));
    }

    @Test
    void shouldNotReturnRoleEditForm() throws Exception {
        this.mockMvc
                .perform(get("/admin/roles/{id}/edit", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/roles"))
                .andExpect(flash().attributeExists("error"));

    }

    @Test
    void shouldUpdateRole() throws Exception {
        Role role = new Role(1L, "test");
        this.mockMvc
                .perform(patch("/admin/roles/{id}", "1")
                        .flashAttr("role", role).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/roles"))
                .andExpect(flash().attributeExists("success"));
        verify(roleService, times(1)).saveRole(role);
    }
}
