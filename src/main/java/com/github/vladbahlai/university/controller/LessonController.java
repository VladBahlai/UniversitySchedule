package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.model.Lesson;
import com.github.vladbahlai.university.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final TimeSpanService timeSpanService;
    private final AudienceService audienceService;
    private final DisciplineService disciplineService;
    Logger logger = LoggerFactory.getLogger(LessonController.class);

    public LessonController(LessonService lessonService, GroupService groupService, TeacherService teacherService, TimeSpanService timeSpanService, AudienceService audienceService, DisciplineService disciplineService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.timeSpanService = timeSpanService;
        this.audienceService = audienceService;
        this.disciplineService = disciplineService;
    }

    @GetMapping
    public String getLessonPage(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "25") int size, Model model) {
        model.addAttribute("lessons", lessonService.getPage(PageRequest.of(page - 1, size)));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        return "lessons/index";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('WRITE_LESSON')")
    public String getNewLessonForm(Model model) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("disciplines", disciplineService.getDisciplines());
        model.addAttribute("groups", groupService.getGroups());
        model.addAttribute("teachers", teacherService.getTeachers());
        model.addAttribute("audiences", audienceService.getAudiences());
        model.addAttribute("timeSpans", timeSpanService.getTimeSpans());
        return "lessons/new";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_LESSON')")
    public String createLesson(Lesson lesson, RedirectAttributes redirectAttributes) {
        lessonService.saveLesson(lesson);
        logger.info("Created lesson " + lesson);
        redirectAttributes.addFlashAttribute("success", "Lesson created.");
        return "redirect:/lessons";
    }

    @DeleteMapping("/{id}")
    public String deleteLesson(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        lessonService.deleteLesson(id);
        logger.info("Deleted lesson with id = " + id);
        redirectAttributes.addFlashAttribute("success", "Lesson deleted.");
        return "redirect:/lessons";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('WRITE_LESSON')")
    public String getEditLessonForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("lesson", lessonService.getLessonById(id));
        model.addAttribute("disciplines", disciplineService.getDisciplines());
        model.addAttribute("groups", groupService.getGroups());
        model.addAttribute("teachers", teacherService.getTeachers());
        model.addAttribute("audiences", audienceService.getAudiences());
        model.addAttribute("timeSpans", timeSpanService.getTimeSpans());
        return "lessons/edit";
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_LESSON')")
    public String updateLesson(Lesson lesson, RedirectAttributes redirectAttributes) {
        lessonService.saveLesson(lesson);
        logger.info("Updated lesson with id = " + lesson.getId());
        redirectAttributes.addFlashAttribute("success", "Lesson updated.");
        return "redirect:/lessons";
    }
}
