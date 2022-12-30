package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.mapper.CalendarMapper;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.service.GroupService;
import com.github.vladbahlai.university.service.LessonService;
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
@RequestMapping("/schedule")
public class GroupScheduleController {

    private final LessonService lessonService;
    private final CalendarMapper calendarMapper;
    private final GroupService groupService;

    public GroupScheduleController(LessonService lessonService, CalendarMapper calendarMapper, GroupService groupService) {
        this.lessonService = lessonService;
        this.calendarMapper = calendarMapper;
        this.groupService = groupService;
    }

    @GetMapping
    public ModelAndView getSchedule() {
        return new ModelAndView("schedules/schedule", "groups", groupService.getGroups());
    }

    @GetMapping(value = "/data")
    @ResponseBody
    public List<CalendarDTO> getScheduleData(@RequestParam(value = "startDate") Optional<String> startDate, @RequestParam(value = "endDate") Optional<String> endDate, Optional<Integer> groupId) {
        LocalDate start = LocalDate.parse(startDate.orElseThrow(() -> new IllegalArgumentException("Start date can't be a null")));
        LocalDate end = LocalDate.parse(endDate.orElseThrow(() -> new IllegalArgumentException("End date can't be a null")));
        Group group = groupService.getGroupById(Long.valueOf(groupId.orElseThrow(() -> new IllegalArgumentException("Group id can't be a null"))));
        return lessonService.getGroupLessons(group, start, end).stream().map(calendarMapper::toCalendarDTO).collect(Collectors.toList());
    }

}

