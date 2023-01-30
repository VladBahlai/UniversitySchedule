package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.service.AudienceService;
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
@RequestMapping("/audiences")
public class AudienceController {

    private final AudienceService audienceService;
    Logger logger = LoggerFactory.getLogger(AudienceController.class);

    public AudienceController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping
    public String getAudiencePage(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("audiences", audienceService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "audiences/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('WRITE_AUDIENCE')")
    public String getNewAudienceForm(Audience audience) {
        return "audiences/new";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_AUDIENCE')")
    public String createAudience(Audience audience, RedirectAttributes redirectAttributes) {
        try {
            audienceService.saveAudience(audience);
            logger.info("Created audience " + audience);
            redirectAttributes.addFlashAttribute("success", "Audience " + audience.getName() + " created.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/audiences";
    }

    @DeleteMapping("/{id}")
    public String deleteAudience(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        audienceService.deleteAudience(id);
        logger.info("Deleted audience with id = " + id);
        redirectAttributes.addFlashAttribute("success", "Audience deleted.");
        return "redirect:/audiences";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('WRITE_AUDIENCE')")
    public ModelAndView getEditAudienceForm(@PathVariable("id") Long id) {
        return new ModelAndView("audiences/edit", "audience", audienceService.getAudienceById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_AUDIENCE')")
    public String updateAudience(Audience audience, RedirectAttributes redirectAttributes) {
        try {
            audienceService.saveAudience(audience);
            logger.info("Updated audience with id = " + audience.getId());
            redirectAttributes.addFlashAttribute("success", "Audience " + audience.getName() + " updated.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/audiences";
    }
}
