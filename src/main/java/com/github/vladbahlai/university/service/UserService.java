package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public interface UserService extends UserDetailsService {

    @Transactional
    User saveUser(User user) throws UniqueConstraintException;

    @Transactional(readOnly = true)
    List<User> getUsers();

    @Transactional(readOnly = true)
    User getUserById(Long id);

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    void deleteUser(Long id);

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional(readOnly = true)
    Page<User> getPage(PageRequest pageRequest);
}
