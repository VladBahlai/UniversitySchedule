package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.dto.CalendarDTO;
import com.github.vladbahlai.university.mapper.CalendarMapper;
import com.github.vladbahlai.university.service.AudienceService;
import com.github.vladbahlai.university.service.LessonService;
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
@RequestMapping("/audienceSchedule")
public class AudienceScheduleController {

    private final LessonService lessonService;
    private final AudienceService audienceService;
    private final CalendarMapper calendarMapper;

    public AudienceScheduleController(LessonService lessonService, AudienceService audienceService, CalendarMapper calendarMapper) {
        this.lessonService = lessonService;
        this.audienceService = audienceService;
        this.calendarMapper = calendarMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_AUDIENCE_SCHEDULE')")
    public ModelAndView getAudienceScheduleView() {
        return new ModelAndView("schedules/audienceSchedule", "audiences", audienceService.getAudiences());
    }

    @GetMapping(value = "/data")
    @ResponseBody
    @PreAuthorize("hasAuthority('READ_AUDIENCE_SCHEDULE')")
    public List<CalendarDTO> getAudienceScheduleData(@RequestParam(value = "startDate") Optional<String> startDate, @RequestParam(value = "endDate") Optional<String> endDate, @RequestParam(value = "audienceId") Optional<Long> audienceId) {
        LocalDate start = LocalDate.parse(startDate.orElseThrow(() -> new IllegalArgumentException("Start date can't be a null")));
        LocalDate end = LocalDate.parse(endDate.orElseThrow(() -> new IllegalArgumentException("End date can't be a null")));
        Long id = audienceId.orElseThrow(() -> new IllegalArgumentException("Id can't be a null"));
        return lessonService.getAudienceLessons(audienceService.getAudienceById(id), start, end).stream().map(calendarMapper::toCalendarDTO).collect(Collectors.toList());
    }

}
