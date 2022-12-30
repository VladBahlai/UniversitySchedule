package com.github.vladbahlai.university.mapper;

import com.github.vladbahlai.university.dto.SpecialtyDTO;
import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.model.Specialty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SpecialtyMapper.class)
class SpecialtyMapperTest {

    @Autowired
    SpecialtyMapper specialtyMapper;

    @Test
    void shouldReturnSpecialtyDTO() {
        Specialty specialty = new Specialty(1L, "testSpecialty", new Department(1L, "testDepartment"));
        SpecialtyDTO expected = new SpecialtyDTO("1", "testSpecialty", "1", "testDepartment");
        SpecialtyDTO actual = specialtyMapper.toSpecialtyDTO(specialty);
        assertEquals(expected, actual);
    }
}
