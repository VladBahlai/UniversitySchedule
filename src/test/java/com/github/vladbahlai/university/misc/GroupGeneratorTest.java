package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = GroupGenerator.class)
class GroupGeneratorTest {

    @MockBean
    GroupService service;

    @Autowired
    GroupGenerator generator;

    @Test
    void shouldGenerateGroups() throws UniqueNameConstraintException {
        List<Specialty> specialtyList = new ArrayList<>(Arrays.asList(new Specialty(), new Specialty()));
        generator.generateGroupData(specialtyList, 3);
        verify(service, times(3 * specialtyList.size() * Course.values().length)).saveGroup(any(Group.class));
    }

}
