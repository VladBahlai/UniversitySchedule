package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    boolean existsByName(String name);

}
