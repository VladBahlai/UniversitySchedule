package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Teacher;
import com.github.vladbahlai.university.service.DepartmentService;
import com.github.vladbahlai.university.service.DisciplineService;
import com.github.vladbahlai.university.service.RoleService;
import com.github.vladbahlai.university.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.HashSet;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final DepartmentService departmentService;
    private final DisciplineService disciplineService;
    private final RoleService roleService;
    Logger logger = LoggerFactory.getLogger(TeacherController.class);

    public TeacherController(TeacherService teacherService, DepartmentService departmentService, DisciplineService disciplineService, RoleService roleService) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
        this.disciplineService = disciplineService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getTeacherPage(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("teachers", teacherService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "teachers/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('WRITE_TEACHER')")
    public String getNewTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("departments", departmentService.getDepartments());
        model.addAttribute("disciplines", disciplineService.getDisciplines());
        return "teachers/new";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_TEACHER')")
    public String createTeacher(Teacher teacher, RedirectAttributes redirectAttributes) {
        teacher.setRoles(new HashSet<>(Collections.singletonList(roleService.getRoleByName("ROLE_TEACHER"))));
        try {
            teacherService.saveTeacher(teacher);
            logger.info("Created teacher " + teacher);
            redirectAttributes.addFlashAttribute("success", "Teacher with name " + teacher.getName() + " created.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        catch (ConstraintViolationException e){
            redirectAttributes.addFlashAttribute("error", "Email is not valid.");
        }
        return "redirect:/teachers";
    }

    @DeleteMapping("/{id}")
    public String deleteTeacher(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        teacherService.deleteTeacher(id);
        logger.info("Deleted teacher with id = " + id);
        redirectAttributes.addFlashAttribute("success", "Teacher deleted.");
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('WRITE_TEACHER')")
    public String getEditTeacherForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("teacher", teacherService.getTeacherById(id));
        model.addAttribute("departments", departmentService.getDepartments());
        model.addAttribute("disciplines", disciplineService.getDisciplines());
        return "teachers/edit";
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_TEACHER')")
    public String updateStudent(@PathVariable("id") Long id, Teacher teacher, RedirectAttributes redirectAttributes) {
        Teacher oldTeacher = teacherService.getTeacherById(id);
        teacher.getRoles().addAll(oldTeacher.getRoles());
        try {
            teacherService.saveTeacher(teacher);
            logger.info("Updated teacher with id = " + teacher.getId());
            redirectAttributes.addFlashAttribute("success", "Teacher with name " + teacher.getName() + " updated.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        catch (ConstraintViolationException e){
            redirectAttributes.addFlashAttribute("error", "Email is not valid.");
        }
        return "redirect:/teachers";
    }
}
