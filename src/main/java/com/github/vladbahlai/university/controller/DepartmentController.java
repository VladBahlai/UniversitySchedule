package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String getDepartmentPAge(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("departments", departmentService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "departments/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('WRITE_DEPARTMENT')")
    public String getNewDepartmentForm(Department department) {
        return "departments/new";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_DEPARTMENT')")
    public String createDepartment(Department department, RedirectAttributes redirectAttributes) {
        try {
            departmentService.saveDepartment(department);
            logger.info("Created department " + department);
            redirectAttributes.addFlashAttribute("success", "Department with name " + department.getName() + " created.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/departments";
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        departmentService.deleteDepartment(id);
        logger.info("Deleted department with id = " + id);
        redirectAttributes.addFlashAttribute("success", "Department deleted.");
        return "redirect:/departments";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('WRITE_DEPARTMENT')")
    public ModelAndView getEditDepartmentForm(@PathVariable("id") Long id) {
        return new ModelAndView("departments/edit", "department", departmentService.getDepartmentById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_DEPARTMENT')")
    public String updateDepartment(Department department, RedirectAttributes redirectAttributes) {
        try {
            departmentService.saveDepartment(department);
            logger.info("Updated department with id = " + department.getId());
            redirectAttributes.addFlashAttribute("success", "Department with name " + department.getName() + " updated.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/departments";
    }

}