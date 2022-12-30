package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.Privilege;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class PrivilegeRepositoryTest {

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Test
    void shouldReadAndWritePrivilege() {
        Privilege expected = privilegeRepository.save(new Privilege("test"));
        Optional<Privilege> actual = privilegeRepository.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

    }
}
