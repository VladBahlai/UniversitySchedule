package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.mapper.CalendarMapper;
import com.github.vladbahlai.university.security.MyUserDetails;
import com.github.vladbahlai.university.service.LessonService;
import com.github.vladbahlai.university.service.StudentService;
import com.github.vladbahlai.university.service.TeacherService;
import com.github.vladbahlai.university.utils.UserChecker;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/mySchedule")
public class UserScheduleController {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final LessonService lessonService;
    private final CalendarMapper calendarMapper;

    public UserScheduleController(StudentService studentService, TeacherService teacherService, LessonService lessonService, CalendarMapper calendarMapper) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.lessonService = lessonService;
        this.calendarMapper = calendarMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_USER_SCHEDULE')")
    public String getMySchedule(@AuthenticationPrincipal MyUserDetails userDetails, RedirectAttributes redirectAttributes) {
        if (UserChecker.isTeacher(userDetails, teacherService) || UserChecker.isStudent(userDetails, studentService)) {
            return "schedules/mySchedule";
        }
        redirectAttributes.addFlashAttribute("error", "You don't have any lessons");
        return "redirect:/";
    }

    @GetMapping(value = "/data")
    @ResponseBody
    @PreAuthorize("hasAuthority('READ_USER_SCHEDULE')")
    public List<CalendarDTO> getMyScheduleData(@RequestParam(value = "startDate") Optional<String> startDate, @RequestParam(value = "endDate") Optional<String> endDate, @AuthenticationPrincipal MyUserDetails userDetails) {
        LocalDate start = LocalDate.parse(startDate.orElse(LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
        LocalDate end = LocalDate.parse(endDate.orElse(LocalDate.now().plusDays(7L).format(DateTimeFormatter.ISO_DATE)));
        if (UserChecker.isTeacher(userDetails, teacherService)) {
            return lessonService.getTeacherLessons(teacherService.getTeacherById(userDetails.getUser().getId()), start, end)
                    .stream().map(calendarMapper::toCalendarDTO).collect(Collectors.toList());
        } else {
            return lessonService.getGroupLessons(studentService.getStudentById(userDetails.getUser().getId()).getGroup(), start, end)
                    .stream().map(calendarMapper::toCalendarDTO).collect(Collectors.toList());
        }
    }
}
