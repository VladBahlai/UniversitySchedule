package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.repository.DisciplineRepository;
import com.github.vladbahlai.university.service.DisciplineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DisciplineServiceImpl implements DisciplineService {

    private final DisciplineRepository repo;

    public DisciplineServiceImpl(DisciplineRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Discipline> getDisciplines() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Discipline getDisciplineById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discipline not found with id - " + id));
    }

    @Override
    @Transactional
    public void deleteDiscipline(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public Discipline saveDiscipline(Discipline discipline) {
        return repo.save(discipline);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Discipline> getPage(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }

    @Override
    public List<Discipline> getAllByCourseAndSpecialty(Course course, Specialty specialty) {
        return repo.findAllByCourseAndSpecialty(course, specialty);
    }

}
