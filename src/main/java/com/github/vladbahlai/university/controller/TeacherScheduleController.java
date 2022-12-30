package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.dto.TeacherDTO;
import com.github.vladbahlai.university.mapper.CalendarMapper;
import com.github.vladbahlai.university.mapper.TeacherMapper;
import com.github.vladbahlai.university.service.DepartmentService;
import com.github.vladbahlai.university.service.LessonService;
import com.github.vladbahlai.university.service.TeacherService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacherSchedule")
public class TeacherScheduleController {

    private final DepartmentService departmentService;
    private final TeacherService teacherService;
    private final LessonService lessonService;
    private final CalendarMapper calendarMapper;
    private final TeacherMapper teacherMapper;

    public TeacherScheduleController(DepartmentService departmentService, TeacherService teacherService, LessonService lessonService, CalendarMapper calendarMapper, TeacherMapper teacherMapper) {
        this.departmentService = departmentService;
        this.teacherService = teacherService;
        this.lessonService = lessonService;
        this.calendarMapper = calendarMapper;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_TEACHER_SCHEDULE')")
    public ModelAndView getScheduleView() {
        return new ModelAndView("schedules/teacherSchedule", "departments", departmentService.getDepartments());
    }

    @GetMapping("/teachersByDepartment")
    @ResponseBody
    @PreAuthorize("hasAuthority('READ_TEACHER_SCHEDULE')")
    public List<TeacherDTO> findAllTeachers(@RequestParam(value = "departmentId", required = true) Long departmentId) {
        return departmentService.getDepartmentById(departmentId).getTeachers().stream().map(teacherMapper::toTeacherDTO).collect(Collectors.toList());

    }

    @GetMapping(value = "/data")
    @ResponseBody
    @PreAuthorize("hasAuthority('READ_TEACHER_SCHEDULE')")
    public List<CalendarDTO> getTeacherScheduleData(@RequestParam(value = "startDate") Optional<String> startDate, @RequestParam(value = "endDate") Optional<String> endDate, @RequestParam(value = "teacherId") Optional<Long> teacherId) {
        LocalDate start = LocalDate.parse(startDate.orElseThrow(() -> new IllegalArgumentException("Start date can't be a null")));
        LocalDate end = LocalDate.parse(endDate.orElseThrow(() -> new IllegalArgumentException("End date can't be a null")));
        Long id = teacherId.orElseThrow(() -> new IllegalArgumentException("Id can't be a null"));
        return lessonService.getTeacherLessons(teacherService.getTeacherById(id), start, end).stream().map(calendarMapper::toCalendarDTO).collect(Collectors.toList());
    }
}
