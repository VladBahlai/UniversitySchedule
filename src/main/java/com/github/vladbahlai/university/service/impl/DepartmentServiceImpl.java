package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.repository.DepartmentRepository;
import com.github.vladbahlai.university.service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repo;

    public DepartmentServiceImpl(DepartmentRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getDepartments() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id - " + id));
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public Department saveDepartment(Department department) throws UniqueConstraintException {
        if (repo.existsByName(department.getName()) && department.getId() == null) {
            throw new UniqueConstraintException("Department with " + department.getName() + " name already exist.");
        } else if (department.getId() != null && repo.existsById(department.getId()) && repo.existsByName(department.getName())) {
            if (!department.getName().equals(repo.findById(department.getId()).get().getName())) {
                throw new UniqueConstraintException("Department with " + department.getName() + " name already exist.");
            }
        }
        return repo.save(department);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Department> getPage(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }
}
