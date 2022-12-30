package com.github.vladbahlai.university.controller;

import com.github.vladbahlai.university.service.AudienceService;
import com.github.vladbahlai.university.service.TimeSpanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/freeAudiences")
public class FreeAudiencesController {

    private final AudienceService audienceService;
    private final TimeSpanService timeSpanService;

    public FreeAudiencesController(AudienceService audienceService, TimeSpanService timeSpanService) {
        this.audienceService = audienceService;
        this.timeSpanService = timeSpanService;
    }

    @GetMapping
    public String findFreeAudiences(@RequestParam(value = "date") Optional<String> date, @RequestParam(value = "timeSpanId") Optional<Long> timeSpanId, Model model) {
        model.addAttribute("timeSpans", timeSpanService.getTimeSpans());
        if (date.isPresent() && timeSpanId.isPresent()) {
            model.addAttribute("audiences", audienceService.getFreeAudiences(
                    LocalDate.parse(date.orElseThrow(() -> new IllegalArgumentException("Date can't be a null."))),
                    timeSpanService.getTimeSpanById(timeSpanId.orElseThrow(() -> new IllegalArgumentException("Id can't be a null.")))));
        }
        return "audiences/free";
    }
}
