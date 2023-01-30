package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.TimeSpan;
import com.github.vladbahlai.university.service.TimeSpanService;
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
@RequestMapping("/timeSpans")
public class TimeSpanController {

    private final TimeSpanService timeSpanService;
    Logger logger = LoggerFactory.getLogger(TimeSpanController.class);

    public TimeSpanController(TimeSpanService timeSpanService) {
        this.timeSpanService = timeSpanService;
    }

    @GetMapping
    public String getTimeSpanPage(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("timeSpans", timeSpanService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "timeSpans/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('WRITE_TIMESPAN')")
    public String getNewTimeSpanForm(TimeSpan timeSpan) {
        return "timeSpans/new";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_TIMESPAN')")
    public String createTimeSpan(TimeSpan timeSpan, RedirectAttributes redirectAttributes) {
        try {
            timeSpanService.saveTimeSpan(timeSpan);
            logger.info("Created timeSpan " + timeSpan);
            redirectAttributes.addFlashAttribute("success", "Time span with number of lesson " + timeSpan.getNumberOfLesson() + " created.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/timeSpans";
    }

    @DeleteMapping("/{id}")
    public String deleteTimeSpan(@PathVariable("id") Long id,RedirectAttributes redirectAttributes) {
        timeSpanService.deleteTimeSpan(id);
        logger.info("Deleted timeSpan with id = " + id);
        redirectAttributes.addFlashAttribute("success", "Time span deleted.");
        return "redirect:/timeSpans";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('WRITE_TIMESPAN')")
    public ModelAndView getEditTimeSpanForm(@PathVariable("id") Long id) {
        return new ModelAndView("timeSpans/edit", "timeSpan", timeSpanService.getTimeSpanById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_TIMESPAN')")
    public String updateStudent(TimeSpan timeSpan, RedirectAttributes redirectAttributes) {
        try {
            timeSpanService.saveTimeSpan(timeSpan);
            logger.info("Updated timeSpan with id = " + timeSpan.getId());
            redirectAttributes.addFlashAttribute("success", "Time span with number of lesson " + timeSpan.getNumberOfLesson() + " updated.");
        } catch (UniqueConstraintException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/timeSpans";
    }

}
