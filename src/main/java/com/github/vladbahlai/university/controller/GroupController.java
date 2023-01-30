package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.service.GroupService;
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
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final SpecialtyService specialtyService;
    Logger logger = LoggerFactory.getLogger(GroupController.class);

    public GroupController(GroupService groupService, SpecialtyService specialtyService) {
        this.groupService = groupService;
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public String getGroupPage(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("groups", groupService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "groups/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('WRITE_GROUP')")
    public String getNewGroupFrom(Model model) {
        model.addAttribute("group", new Group());
        model.addAttribute("specialties", specialtyService.getSpecialties());
        return "groups/new";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_GROUP')")
    public String createGroup(Group group, RedirectAttributes redirectAttributes) {
        try {
            groupService.saveGroup(group);
            logger.info("Created group " + group);
            redirectAttributes.addFlashAttribute("success", "Group with name " + group.getName() + " created.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/groups";
    }

    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        groupService.deleteGroup(id);
        logger.info("Deleted group with id = " + id);
        redirectAttributes.addFlashAttribute("success", "Group deleted.");
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('WRITE_GROUP')")
    public String getEditGroupForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("group", groupService.getGroupById(id));
        model.addAttribute("specialties", specialtyService.getSpecialties());
        return "groups/edit";
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_GROUP')")
    public String updateGroup(Group group, RedirectAttributes redirectAttributes) {
        try {
            groupService.saveGroup(group);
            logger.info("Updated group with id = " + group.getId());
            redirectAttributes.addFlashAttribute("success", "Group with name " + group.getName() + " created.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/groups";
    }

}
