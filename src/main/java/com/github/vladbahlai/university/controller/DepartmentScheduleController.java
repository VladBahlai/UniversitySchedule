package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.mapper.CalendarMapper;
import com.github.vladbahlai.university.security.MyUserDetails;
import com.github.vladbahlai.university.service.LessonService;
import com.github.vladbahlai.university.service.TeacherService;
import com.github.vladbahlai.university.utils.UserChecker;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/departmentSchedule")
public class DepartmentScheduleController {

    private final TeacherService teacherService;
    private final LessonService lessonService;
    private final CalendarMapper lessonMapper;

    public DepartmentScheduleController(TeacherService teacherService, LessonService lessonService, CalendarMapper lessonMapper) {
        this.teacherService = teacherService;
        this.lessonService = lessonService;
        this.lessonMapper = lessonMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_TEACHER_DEPARTMENT_SCHEDULE')")
    public String getDepartmentSchedule(@AuthenticationPrincipal MyUserDetails userDetails, Model model, RedirectAttributes redirectAttributes) {
        if (!UserChecker.isTeacher(userDetails, teacherService)) {
            redirectAttributes.addFlashAttribute("error", "You are not teacher! If this wrong, tell about this problem to staff or admin.");
            return "redirect:/";
        }
        model.addAttribute("department", teacherService.getTeacherById(userDetails.getUser().getId()).getDepartment());
        return "schedules/departmentSchedule";
    }

    @GetMapping("/data")
    @ResponseBody
    @PreAuthorize("hasAuthority('READ_TEACHER_DEPARTMENT_SCHEDULE')")
    public List<CalendarDTO> getDepartmentScheduleData(@RequestParam(name = "startDate") Optional<String> startDate, @RequestParam(name = "endDate") Optional<String> endDate, @AuthenticationPrincipal MyUserDetails userDetails) {
        LocalDate start = LocalDate.parse(startDate.orElse(LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
        LocalDate end = LocalDate.parse(endDate.orElse(LocalDate.now().plusDays(7).format(DateTimeFormatter.ISO_DATE)));
        return lessonService.getDepartmentLessons(teacherService.getTeacherById(userDetails.getUser().getId()).getDepartment(), start, end).stream().map(lessonMapper::toCalendarDTO).collect(Collectors.toList());
    }
}
