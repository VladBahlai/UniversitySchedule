package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentService {

    @Transactional(readOnly = true)
    List<Student> getStudents();

    @Transactional(readOnly = true)
    Student getStudentById(Long id);

    @PreAuthorize("hasAuthority('DELETE_STUDENT')")
    @Transactional
    void deleteStudent(Long id);

    @Transactional
    Student saveStudent(Student student) throws UniqueConstraintException;

    @PreAuthorize("hasAuthority('READ_STUDENT')")
    @Transactional(readOnly = true)
    Page<Student> getPage(PageRequest pageRequest);
}