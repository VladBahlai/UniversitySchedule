package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepartmentService {

    @Transactional(readOnly = true)
    List<Department> getDepartments();

    @Transactional(readOnly = true)
    Department getDepartmentById(Long id);

    @PreAuthorize("hasAuthority('DELETE_DEPARTMENT')")
    @Transactional
    void deleteDepartment(Long id);

    @Transactional
    Department saveDepartment(Department department) throws UniqueConstraintException;

    @PreAuthorize("hasAuthority('READ_DEPARTMENT')")
    @Transactional(readOnly = true)
    Page<Department> getPage(PageRequest pageRequest);

}