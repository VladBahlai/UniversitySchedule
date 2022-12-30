package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Student;
import com.github.vladbahlai.university.model.Teacher;
import com.github.vladbahlai.university.security.MyUserDetails;
import com.github.vladbahlai.university.service.DisciplineService;
import com.github.vladbahlai.university.service.SpecialtyService;
import com.github.vladbahlai.university.service.StudentService;
import com.github.vladbahlai.university.service.TeacherService;
import com.github.vladbahlai.university.utils.UserChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/disciplines")
public class DisciplineController {

    private final DisciplineService disciplineService;
    private final SpecialtyService specialtyService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    Logger logger = LoggerFactory.getLogger(DisciplineController.class);

    public DisciplineController(DisciplineService disciplineService, SpecialtyService specialtyService, TeacherService teacherService, StudentService studentService) {
        this.disciplineService = disciplineService;
        this.specialtyService = specialtyService;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @GetMapping
    public String getDisciplinePage(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("disciplines", disciplineService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "disciplines/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('WRITE_DISCIPLINE')")
    public String getNewDisciplineForm(Model model) {
        model.addAttribute("discipline", new Discipline());
        model.addAttribute("specialties", specialtyService.getSpecialties());
        return "disciplines/new";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_DISCIPLINE')")
    public String createDiscipline(Discipline discipline,RedirectAttributes redirectAttributes) {
        disciplineService.saveDiscipline(discipline);
        logger.info("Created discipline " + discipline);
        redirectAttributes.addFlashAttribute("success","Discipline created");
        return "redirect:/disciplines";
    }

    @DeleteMapping("/{id}")
    public String deleteDiscipline(@PathVariable("id") Long id,RedirectAttributes redirectAttributes) {
        disciplineService.deleteDiscipline(id);
        logger.info("Deleted discipline with id = " + id);
        redirectAttributes.addFlashAttribute("success","Discipline deleted.");
        return "redirect:/disciplines";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('WRITE_DISCIPLINE')")
    public String getEditDisciplineForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("discipline", disciplineService.getDisciplineById(id));
        model.addAttribute("specialties", specialtyService.getSpecialties());
        return "disciplines/edit";
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_DISCIPLINE')")
    public String updateDiscipline(Discipline discipline, RedirectAttributes redirectAttributes) {
        disciplineService.saveDiscipline(discipline);
        logger.info("Updated discipline with id = " + discipline.getId());
        redirectAttributes.addFlashAttribute("success","Discipline updated.");
        return "redirect:/disciplines";
    }

    @GetMapping("/my")
    public String findUserDisciplines(@AuthenticationPrincipal MyUserDetails userDetails, Model model, RedirectAttributes redirectAttributes) {
        if (UserChecker.isStudent(userDetails, studentService)) {
            Student student = studentService.getStudentById(userDetails.getUser().getId());
            model.addAttribute("disciplines", disciplineService.getAllByCourseAndSpecialty(student.getGroup().getCourse(), student.getGroup().getSpecialty()));
        } else if (UserChecker.isTeacher(userDetails, teacherService)) {
            Teacher teacher = teacherService.getTeacherById(userDetails.getUser().getId());
            model.addAttribute("disciplines", teacher.getDisciplines());
        } else {
            redirectAttributes.addFlashAttribute("error", "You don't have any disciplines. If you student or teacher tell about this to staff.");
            return "redirect:/";
        }
        return "disciplines/userDisciplines";
    }

}
