package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.repository.LessonRepository;
import com.github.vladbahlai.university.service.LessonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository repo;

    public LessonServiceImpl(LessonRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getLessons() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Lesson getLessonById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Lesson not found with id - " + id));
    }

    @Override
    @Transactional
    public void deleteLesson(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public Lesson saveLesson(Lesson lesson) {
        return repo.save(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Lesson> getPage(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getTeacherLessons(Teacher teacher, LocalDate start, LocalDate end) {
        return repo.findByTeacherAndDateBetween(teacher, start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getGroupLessons(Group group, LocalDate start, LocalDate end) {
        return repo.findByGroupAndDateBetween(group, start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getDepartmentLessons(Department department, LocalDate start, LocalDate end) {
        List<Lesson> lessons = new ArrayList<>();
        for (Teacher teacher : department.getTeachers()) {
            lessons.addAll(repo.findByTeacherAndDateBetween(teacher, start, end));
        }
        return lessons;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getAudienceLessons(Audience audience, LocalDate start, LocalDate end) {
        return repo.findByAudienceAndDateBetween(audience, start, end);
    }
}
