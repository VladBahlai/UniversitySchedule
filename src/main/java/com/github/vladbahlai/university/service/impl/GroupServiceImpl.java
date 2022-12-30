package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.repository.GroupRepository;
import com.github.vladbahlai.university.service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository repo;

    public GroupServiceImpl(GroupRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> getGroups() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Group getGroupById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Group not found with id - " + id));
    }

    @Override
    @Transactional
    public void deleteGroup(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public Group saveGroup(Group group) throws UniqueNameConstraintException {
        if (repo.existsByName(group.getName()) && group.getId() == null) {
            throw new UniqueNameConstraintException("Group with " + group.getName() + " name already exist.");
        } else if (group.getId() != null && repo.existsById(group.getId()) && repo.existsByName(group.getName())) {
            if (!group.getName().equals(repo.findById(group.getId()).get().getName())) {
                throw new UniqueNameConstraintException("Group with " + group.getName() + " name already exist.");
            }
        }
        return repo.save(group);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Group> getPage(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }

}
