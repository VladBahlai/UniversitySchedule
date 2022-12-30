package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.model.Role;
import com.github.vladbahlai.university.service.PrivilegeService;
import com.github.vladbahlai.university.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/roles")
public class RoleController {

    private final RoleService roleService;
    private final PrivilegeService privilegeService;
    Logger logger = LoggerFactory.getLogger(RoleController.class);

    public RoleController(RoleService roleService, PrivilegeService privilegeService) {
        this.roleService = roleService;
        this.privilegeService = privilegeService;
    }

    @GetMapping()
    public String findAllRoles(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("roles", roleService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "roles/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getNewRoleForm(Model model) {
        model.addAttribute("role", new Role());
        model.addAttribute("privileges", privilegeService.getPrivileges());
        return "roles/new";
    }

    @PostMapping()
    public String createRole(Role role, RedirectAttributes redirectAttributes) {
        roleService.saveRole(role);
        logger.info("Created role " + role);
        redirectAttributes.addFlashAttribute("success", "Role with name " + role.getName() + " created.");
        return "redirect:/admin/roles";
    }

    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        roleService.deleteRole(id);
        logger.info("Deleted role with id = " + id);
        redirectAttributes.addFlashAttribute("success","Role deleted.");
        return "redirect:/admin/roles";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditRoleForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        if (id == 1L) {
            redirectAttributes.addFlashAttribute("error","You can't edit this role.");
            return "redirect:/admin/roles";
        }
        model.addAttribute("role", roleService.getRoleById(id));
        model.addAttribute("privileges", privilegeService.getPrivileges());
        return "roles/edit";
    }

    @PatchMapping("/{id}")
    public String updateRole(Role role, RedirectAttributes redirectAttributes) {
        roleService.saveRole(role);
        logger.info("Updated role with id = " + role.getId());
        redirectAttributes.addFlashAttribute("success", "Role with name " + role.getName() + " updated.");
        return "redirect:/admin/roles";
    }
}
