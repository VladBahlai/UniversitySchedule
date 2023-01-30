package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.service.DepartmentService;
import com.github.vladbahlai.university.service.SpecialtyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    private final DepartmentService departmentService;
    Logger logger = LoggerFactory.getLogger(SpecialtyController.class);

    public SpecialtyController(SpecialtyService specialtyService, DepartmentService departmentService) {
        this.specialtyService = specialtyService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String getSpecialtyPage(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("specialties", specialtyService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "specialties/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('WRITE_SPECIALTY')")
    public String getNewSpecialtyForm(Model model) {
        model.addAttribute("specialty", new Specialty());
        model.addAttribute("departments", departmentService.getDepartments());
        return "specialties/new";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_SPECIALTY')")
    public String createSpecialty(Specialty specialty, RedirectAttributes redirectAttributes) {
        try {
            specialtyService.saveSpecialty(specialty);
            logger.info("Created specialty " + specialty);
            redirectAttributes.addFlashAttribute("success", "Specialty with name " + specialty.getName() + " created.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/specialties";
    }

    @DeleteMapping("/{id}")
    public String deleteSpecialty(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        specialtyService.deleteSpecialty(id);
        logger.info("Deleted lesson with id = " + id);
        redirectAttributes.addFlashAttribute("success", "Specialty deleted.");
        return "redirect:/specialties";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('WRITE_SPECIALTY')")
    public String getEditSpecialtyForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("specialty", specialtyService.getSpecialtyById(id));
        model.addAttribute("departments", departmentService.getDepartments());
        return "specialties/edit";
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_SPECIALTY')")
    public String updateSpecialty(Specialty specialty, RedirectAttributes redirectAttributes) {
        try {
            specialtyService.saveSpecialty(specialty);
            logger.info("Updated lesson with id = " + specialty.getId());
            redirectAttributes.addFlashAttribute("success", "Specialty with name " + specialty.getName() + " updated.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/specialties";
    }
}
