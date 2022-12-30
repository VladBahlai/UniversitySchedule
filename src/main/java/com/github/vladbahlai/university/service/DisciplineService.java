package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DisciplineService {

    @Transactional(readOnly = true)
    List<Discipline> getDisciplines();

    @Transactional(readOnly = true)
    Discipline getDisciplineById(Long id);

    @PreAuthorize("hasAuthority('DELETE_DISCIPLINE')")
    @Transactional
    void deleteDiscipline(Long id);

    @Transactional
    Discipline saveDiscipline(Discipline discipline);

    @PreAuthorize("hasAuthority('READ_DISCIPLINE')")
    @Transactional(readOnly = true)
    Page<Discipline> getPage(PageRequest pageRequest);

    @Transactional(readOnly = true)
    List<Discipline> getAllByCourseAndSpecialty(Course course, Specialty specialty);

}