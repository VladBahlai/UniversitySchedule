package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class GroupRepositoryTest {

    @Autowired
    GroupRepository repo;

    @Test
    void shouldReadAndWriteGroup() {
        Group expected = repo.save(new Group("PZ-32", Course.FIRST));
        Optional<Group> actual = repo.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

    }

}
