package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SpecialtyService {

    @Transactional(readOnly = true)
    List<Specialty> getSpecialties();

    @Transactional(readOnly = true)
    Specialty getSpecialtyById(Long id);

    @PreAuthorize("hasAuthority('DELETE_SPECIALTY')")
    @Transactional
    void deleteSpecialty(Long id);

    @Transactional
    Specialty saveSpecialty(Specialty specialty) throws UniqueNameConstraintException;

    @PreAuthorize("hasAuthority('READ_SPECIALTY')")
    @Transactional(readOnly = true)
    Page<Specialty> getPage(PageRequest pageRequest);

}