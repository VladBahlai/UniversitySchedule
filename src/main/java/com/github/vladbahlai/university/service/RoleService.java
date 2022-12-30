package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface RoleService {

    @Transactional(readOnly = true)
    List<Role> getRoles();

    @Transactional(readOnly = true)
    Role getRoleById(Long id);

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    void deleteRole(Long id);

    @Transactional(readOnly = true)
    Role getRoleByName(String name);

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    Role saveRole(Role role);

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional(readOnly = true)
    Page<Role> getPage(PageRequest pageRequest);
}
