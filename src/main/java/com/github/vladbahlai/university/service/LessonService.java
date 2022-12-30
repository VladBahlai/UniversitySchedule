package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface LessonService {

    @Transactional(readOnly = true)
    List<Lesson> getLessons();

    @Transactional(readOnly = true)
    Lesson getLessonById(Long id);

    @PreAuthorize("hasAuthority('DELETE_LESSON')")
    @Transactional
    void deleteLesson(Long id);

    @Transactional
    Lesson saveLesson(Lesson lesson);

    @PreAuthorize("hasAuthority('READ_LESSON')")
    @Transactional(readOnly = true)
    Page<Lesson> getPage(PageRequest pageRequest);

    @Transactional(readOnly = true)
    List<Lesson> getTeacherLessons(Teacher teacher, LocalDate start, LocalDate end);

    @Transactional(readOnly = true)
    List<Lesson> getGroupLessons(Group group, LocalDate start, LocalDate end);

    @Transactional(readOnly = true)
    List<Lesson> getDepartmentLessons(Department department, LocalDate start, LocalDate end);

    @Transactional(readOnly = true)
    List<Lesson> getAudienceLessons(Audience audience, LocalDate start, LocalDate end);

}