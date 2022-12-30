package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.model.Privilege;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface PrivilegeService {

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional(readOnly = true)
    List<Privilege> getPrivileges();

}
