package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.TimeSpan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TimeSpanService {

    @Transactional(readOnly = true)
    List<TimeSpan> getTimeSpans();

    @Transactional(readOnly = true)
    TimeSpan getTimeSpanById(Long id);

    @PreAuthorize("hasAuthority('DELETE_TIMESPAN')")
    @Transactional
    void deleteTimeSpan(Long id);

    @Transactional
    TimeSpan saveTimeSpan(TimeSpan timeSpan) throws UniqueConstraintException;

    @PreAuthorize("hasAuthority('READ_TIMESPAN')")
    @Transactional(readOnly = true)
    Page<TimeSpan> getPage(PageRequest pageRequest);

}