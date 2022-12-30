package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.repository.SpecialtyRepository;
import com.github.vladbahlai.university.service.SpecialtyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository repo;

    public SpecialtyServiceImpl(SpecialtyRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Specialty> getSpecialties() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Specialty getSpecialtyById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Specialty not found with id - " + id));
    }

    @Override
    @Transactional
    public void deleteSpecialty(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public Specialty saveSpecialty(Specialty specialty) throws UniqueNameConstraintException {
        if (repo.existsByName(specialty.getName()) && specialty.getId() == null) {
            throw new UniqueNameConstraintException("Specialty with " + specialty.getName() + " name already exist.");
        } else if (specialty.getId() != null && repo.existsById(specialty.getId()) && repo.existsByName(specialty.getName())) {
            if (!specialty.getName().equals(repo.findById(specialty.getId()).get().getName())) {
                throw new UniqueNameConstraintException("Specialty with " + specialty.getName() + " name already exist.");
            }
        }
        return repo.save(specialty);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Specialty> getPage(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }
}
