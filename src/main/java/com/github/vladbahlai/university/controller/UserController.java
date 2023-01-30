package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.User;
import com.github.vladbahlai.university.service.RoleService;
import com.github.vladbahlai.university.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.ConstraintViolationException;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final RoleService roleService;
    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping()
    public String getUserPage(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("users", userService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "users/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getNewUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getRoles());
        return "users/new";
    }

    @PostMapping()
    public String createUser(User user, RedirectAttributes redirectAttributes) {
        try {
            userService.saveUser(user);
            logger.info("Created user with name " + user.getName());
            redirectAttributes.addFlashAttribute("success", "User with name " + user.getName() + " created.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        catch (ConstraintViolationException e){
            redirectAttributes.addFlashAttribute("error", "Email is not valid.");
        }
        return "redirect:/admin/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (id != 1L) {
            userService.deleteUser(id);
        }
        logger.info("Deleted user with id = " + id);
        redirectAttributes.addFlashAttribute("success", "User deleted.");
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditUserForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        if (id == 1L) {
            redirectAttributes.addFlashAttribute("error", "You can't edit this user.");
            return "redirect:/admin/users";
        }
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getRoles());
        return "users/edit";
    }

    @PatchMapping("/{id}")
    public String updateRole(User user, RedirectAttributes redirectAttributes) {
        try {
            userService.saveUser(user);
            logger.info("Updated user with id " + user.getId());
            redirectAttributes.addFlashAttribute("success", "User with name " + user.getName() + " updated.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        catch (ConstraintViolationException e){
            redirectAttributes.addFlashAttribute("error", "Email is not valid.");
        }
        return "redirect:/admin/users";
    }
}
