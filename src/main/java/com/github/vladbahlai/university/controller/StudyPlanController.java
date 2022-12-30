package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.SpecialtyDTO;
import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.mapper.SpecialtyMapper;
import com.github.vladbahlai.university.service.DepartmentService;
import com.github.vladbahlai.university.service.DisciplineService;
import com.github.vladbahlai.university.service.SpecialtyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/studyPlan")
@PreAuthorize("hasAuthority('READ_STUDY_PLAN')")
public class StudyPlanController {

    private final DepartmentService departmentService;
    private final SpecialtyService specialtyService;
    private final DisciplineService disciplineService;
    private final SpecialtyMapper specialtyMapper;

    public StudyPlanController(DepartmentService departmentService, SpecialtyService specialtyService, DisciplineService disciplineService, SpecialtyMapper specialtyMapper) {
        this.departmentService = departmentService;
        this.specialtyService = specialtyService;
        this.disciplineService = disciplineService;
        this.specialtyMapper = specialtyMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_STUDY_PLAN')")
    public String getStudyPlan(@RequestParam(name = "specialtyId") Optional<Long> specialtyId, @RequestParam(name = "course") Optional<String> course, Model model) {
        model.addAttribute("departments", departmentService.getDepartments());
        model.addAttribute("courses", Course.values());
        if (specialtyId.isPresent() && course.isPresent()) {
            model.addAttribute("disciplines",
                    disciplineService.getAllByCourseAndSpecialty(
                            Course.valueOf(course
                                    .orElseThrow(() -> new IllegalArgumentException("Course can't be a null."))),
                            specialtyService.getSpecialtyById(specialtyId
                                    .orElseThrow(() -> new IllegalArgumentException("Id can't be a null")))));
        }
        return "disciplines/studyPlan";
    }

    @GetMapping("/specialtiesByDepartment")
    @ResponseBody
    @PreAuthorize("hasAuthority('READ_STUDY_PLAN')")
    public List<SpecialtyDTO> findAllSpecialties(@RequestParam(value = "departmentId", required = true) Long departmentId) {
        return specialtyService.getSpecialties().stream().filter(specialty -> specialty.getDepartment().equals(departmentService.getDepartmentById(departmentId))).collect(Collectors.toList()).stream().map(specialtyMapper::toSpecialtyDTO).collect(Collectors.toList());
    }

}
