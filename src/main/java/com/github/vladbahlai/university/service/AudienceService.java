package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.model.TimeSpan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface AudienceService {

    @Transactional(readOnly = true)
    List<Audience> getAudiences();

    @Transactional(readOnly = true)
    Audience getAudienceById(Long id);

    @PreAuthorize("hasAuthority('DELETE_AUDIENCE')")
    @Transactional
    void deleteAudience(Long id);

    @Transactional
    Audience saveAudience(Audience audience) throws UniqueConstraintException;

    @PreAuthorize("hasAuthority('READ_AUDIENCE')")
    @Transactional(readOnly = true)
    Page<Audience> getPage(PageRequest pageRequest);

    @Transactional(readOnly = true)
    List<Audience> getFreeAudiences(LocalDate date, TimeSpan timeSpan);

}