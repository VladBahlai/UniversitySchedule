package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.model.Role;
import com.github.vladbahlai.university.model.User;
import com.github.vladbahlai.university.service.RoleService;
import com.github.vladbahlai.university.service.UserService;
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

@WebMvcTest(UserController.class)
@WithMockUser(username = "test", authorities = "ADMIN")
class UserControllerTest {

    @MockBean
    UserService userService;
    @MockBean
    RoleService roleService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnViewWithUserPage() throws Exception {
        List<User> userList = Arrays.asList(new User("test", "test"), new User("test", "test"));
        Page<User> users = new PageImpl<>(userList);
        when(userService.getPage(PageRequest.of(0, 25))).thenReturn(users);
        this.mockMvc
                .perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/index"))
                .andExpect(model().attribute("users", users))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("currentSize", 25));
    }

    @Test
    void shouldReturnUserCreateForm() throws Exception {
        List<Role> roles = Arrays.asList(new Role(1L, "test"), new Role(2L, "test"));
        when(roleService.getRoles()).thenReturn(roles);
        this.mockMvc
                .perform(get("/admin/users/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/new"))
                .andExpect(model().attribute("user", new User()))
                .andExpect(model().attribute("roles", roles));
    }

    @Test
    void shouldCreateUser() throws Exception {
        User user = new User("test", "test");
        this.mockMvc
                .perform(post("/admin/users")
                        .flashAttr("user", user).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/users"))
                .andExpect(flash().attributeExists("success"));

        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void shouldDeleteUser() throws Exception {
        this.mockMvc
                .perform(delete("/admin/users/{id}", "2").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/users"))
                .andExpect(flash().attributeExists("success"));
        verify(userService, times(1)).deleteUser(2L);
    }

    @Test
    void shouldReturnUserEditForm() throws Exception {
        List<Role> roles = Arrays.asList(new Role(1L, "test"), new Role(2L, "test"));
        User user = new User(2L, "test", "test");

        when(roleService.getRoles()).thenReturn(roles);
        when(userService.getUserById(2L)).thenReturn(user);
        this.mockMvc
                .perform(get("/admin/users/{id}/edit", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/edit"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("roles", roles));
    }

    @Test
    void shouldNotReturnUserEditForm() throws Exception {
        this.mockMvc
                .perform(get("/admin/users/{id}/edit", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/users"))
                .andExpect(flash().attributeExists("error"));

    }

    @Test
    void shouldUpdateUserWithoutPassword() throws Exception {
        User user = new User(2L, "test", "test");
        when(userService.getUserById(2L)).thenReturn(user);
        this.mockMvc
                .perform(patch("/admin/users/{id}", "2")
                        .flashAttr("user", user).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/users"))
                .andExpect(flash().attributeExists("success"));

        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void shouldUpdateUserWithPassword() throws Exception {
        User userWithNewPassword = new User(2L, "test", "test");
        User userWithOldPassword = new User(2L, "test", "tes");
        when(userService.getUserById(2L)).thenReturn(userWithOldPassword);
        this.mockMvc
                .perform(patch("/admin/users/{id}", "2")
                        .flashAttr("user", userWithNewPassword).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/users"))
                .andExpect(flash().attributeExists("success"));

        verify(userService, times(1)).saveUser(userWithNewPassword);
    }
}
