package com.github.vladbahlai.university.service;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.repository.GroupRepository;
import com.github.vladbahlai.university.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GroupServiceImpl.class)
class GroupServiceTest {

    @MockBean
    GroupRepository repository;
    @Autowired
    GroupService service;

    @Test
    void shouldCreateGroup() throws UniqueNameConstraintException {
        Group group = new Group("1", Course.FIRST);
        Group expected = new Group(1L, "1", Course.FIRST);
        when(repository.existsByName(group.getName())).thenReturn(false);
        when(repository.save(group)).thenReturn(new Group(1L, "1", Course.FIRST));
        Group actual = service.saveGroup(group);
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateGroup() throws UniqueNameConstraintException {
        Group expected = new Group(1L, "1", Course.FIRST);
        when(repository.existsByName(expected.getName())).thenReturn(true);
        when(repository.existsById(expected.getId())).thenReturn(true);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(repository.save(expected)).thenReturn(expected);
        Group actual = service.saveGroup(expected);
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowUniqueNameConstraintException() {
        Group firstGroup = new Group("a", Course.FIRST);
        Group secondGroup = new Group(1L, "a", Course.FIRST);
        when(repository.existsByName(firstGroup.getName())).thenReturn(true);
        Exception firstException = assertThrows(UniqueNameConstraintException.class, () -> service.saveGroup(firstGroup));
        when(repository.existsByName(secondGroup.getName())).thenReturn(true);
        when(repository.existsById(secondGroup.getId())).thenReturn(true);
        when(repository.findById(secondGroup.getId())).thenReturn(Optional.of(new Group(1L, "2", Course.FIRST)));
        Exception secondException = assertThrows(UniqueNameConstraintException.class, () -> service.saveGroup(secondGroup));
        String expectedExceptionMessage = "Group with a name already exist.";
        assertEquals(expectedExceptionMessage, firstException.getMessage());
        assertEquals(expectedExceptionMessage, secondException.getMessage());

    }
}
