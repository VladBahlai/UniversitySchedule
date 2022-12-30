package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.service.DisciplineService;
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

@SpringBootTest(classes = DisciplineGenerator.class)
class DisciplineGeneratorTest {

    @MockBean
    DisciplineService service;

    @Autowired
    DisciplineGenerator generator;


    @Test
    void shouldGenerateDisciplines() {
        List<Specialty> specialtyList = new ArrayList<>(Arrays.asList(new Specialty(), new Specialty()));
        generator.generateDisciplineData(specialtyList, 5);
        verify(service, times(5 * specialtyList.size() * Course.values().length)).saveDiscipline(any(Discipline.class));
    }
}
