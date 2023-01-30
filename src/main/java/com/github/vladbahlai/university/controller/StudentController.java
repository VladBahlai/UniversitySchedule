package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Student;
import com.github.vladbahlai.university.service.GroupService;
import com.github.vladbahlai.university.service.RoleService;
import com.github.vladbahlai.university.service.StudentService;
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
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final GroupService groupService;
    private final RoleService roleService;
    Logger logger = LoggerFactory.getLogger(StudentController.class);

    public StudentController(StudentService studentService, GroupService groupService, RoleService roleService) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getStudentPage(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("students", studentService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "students/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('WRITE_STUDENT')")
    public String getNewStudentForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.getGroups());
        return "students/new";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_STUDENT')")
    public String createStudent(Student student, RedirectAttributes redirectAttributes) {
        student.setRoles(new HashSet<>(Collections.singletonList(roleService.getRoleByName("ROLE_STUDENT"))));
        try {
            studentService.saveStudent(student);
            logger.info("Created student " + student);
            redirectAttributes.addFlashAttribute("success", "Student with name " + student.getName() + " created.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        catch (ConstraintViolationException e){
            redirectAttributes.addFlashAttribute("error", "Email is not valid.");
        }
        return "redirect:/students";
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteStudent(id);
        logger.info("Deleted student with id = " + id);
        redirectAttributes.addFlashAttribute("success", "Student deleted.");
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('WRITE_STUDENT')")
    public String getEditStudentForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("groups", groupService.getGroups());
        return "students/edit";
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_STUDENT')")
    public String updateStudent(@PathVariable("id") Long id, Student student, RedirectAttributes redirectAttributes) {
        Student oldStudent = studentService.getStudentById(id);
        student.getRoles().addAll(oldStudent.getRoles());
        try {
            studentService.saveStudent(student);
            logger.info("Updated student with id = " + student.getId());
            redirectAttributes.addFlashAttribute("success", "Student with name " + student.getName() + " updated.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        catch (ConstraintViolationException e){
            redirectAttributes.addFlashAttribute("error", "Email is not valid.");
        }
        return "redirect:/students";
    }
}
