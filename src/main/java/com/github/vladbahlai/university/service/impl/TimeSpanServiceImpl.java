package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.TimeSpan;
import com.github.vladbahlai.university.repository.TimeSpanRepository;
import com.github.vladbahlai.university.service.TimeSpanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TimeSpanServiceImpl implements TimeSpanService {

    private final TimeSpanRepository repo;

    public TimeSpanServiceImpl(TimeSpanRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeSpan> getTimeSpans() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TimeSpan getTimeSpanById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Time span not found with id - " + id));
    }

    @Override
    @Transactional
    public void deleteTimeSpan(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public TimeSpan saveTimeSpan(TimeSpan timeSpan) throws UniqueConstraintException {
        if (repo.existsByNumberOfLesson(timeSpan.getNumberOfLesson()) && timeSpan.getId() == null) {
            throw new UniqueConstraintException("TimeSpan with " + timeSpan.getNumberOfLesson() + " number already exist.");
        } else if (timeSpan.getId() != null && repo.existsById(timeSpan.getId()) && repo.existsByNumberOfLesson(timeSpan.getNumberOfLesson())) {
            if (timeSpan.getNumberOfLesson() != repo.findById(timeSpan.getId()).get().getNumberOfLesson()) {
                throw new UniqueConstraintException("TimeSpan with " + timeSpan.getNumberOfLesson() + " number already exist.");
            }
        }
        return repo.save(timeSpan);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TimeSpan> getPage(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }

}
