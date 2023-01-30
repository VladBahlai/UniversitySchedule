package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.model.TimeSpan;
import com.github.vladbahlai.university.repository.AudienceRepository;
import com.github.vladbahlai.university.service.AudienceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AudienceServiceImpl implements AudienceService {

    private final AudienceRepository repo;

    public AudienceServiceImpl(AudienceRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Audience> getAudiences() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Audience getAudienceById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Audience not found with id - " + id));
    }

    @Override
    @Transactional
    public void deleteAudience(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public Audience saveAudience(Audience audience) throws UniqueConstraintException {
        if (repo.existsByName(audience.getName()) && audience.getId() == null) {
            throw new UniqueConstraintException("Audience with " + audience.getName() + " name already exist.");
        } else if (audience.getId() != null && repo.existsById(audience.getId()) && repo.existsByName(audience.getName())) {
            if (!audience.getName().equals(repo.findById(audience.getId()).get().getName())) {
                throw new UniqueConstraintException("Audience with " + audience.getName() + " name already exist.");
            }
        }
        return repo.save(audience);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Audience> getPage(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Audience> getFreeAudiences(LocalDate date, TimeSpan timeSpan) {
        List<Audience> audiences = repo.findAll();
        audiences.removeAll(repo.findAudiencesByDateAndTimeSpan(date, timeSpan));
        return audiences;
    }

}
