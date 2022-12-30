package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.model.Privilege;
import com.github.vladbahlai.university.repository.PrivilegeRepository;
import com.github.vladbahlai.university.service.PrivilegeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Privilege> getPrivileges() {
        return privilegeRepository.findAll();
    }

}
