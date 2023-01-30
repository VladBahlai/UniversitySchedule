package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeacherService {

    @Transactional(readOnly = true)
    List<Teacher> getTeachers();

    @Transactional(readOnly = true)
    Teacher getTeacherById(Long id);

    @PreAuthorize("hasAuthority('DELETE_TEACHER')")
    @Transactional
    void deleteTeacher(Long id);

    @Transactional
    Teacher saveTeacher(Teacher teacher) throws UniqueConstraintException;

    @Transactional
    Teacher addDisciplineToTeacher(Long teacherId, Long disciplineId);

    @Transactional
    Teacher deleteDisciplineFromTeacher(Long teacherId, Long disciplineId);

    @PreAuthorize("hasAuthority('READ_TEACHER')")
    @Transactional(readOnly = true)
    Page<Teacher> getPage(PageRequest pageRequest);

}