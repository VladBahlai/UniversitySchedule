package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GroupService {

    @Transactional(readOnly = true)
    List<Group> getGroups();

    @Transactional(readOnly = true)
    Group getGroupById(Long id);

    @PreAuthorize("hasAuthority('DELETE_GROUP')")
    @Transactional
    void deleteGroup(Long id);

    @Transactional
    Group saveGroup(Group group) throws UniqueConstraintException;

    @PreAuthorize("hasAuthority('READ_GROUP')")
    @Transactional(readOnly = true)
    Page<Group> getPage(PageRequest pageRequest);

}