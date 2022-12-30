package com.github.vladbahlai.university.mapper;

import com.github.vladbahlai.university.dto.SpecialtyDTO;
import com.github.vladbahlai.university.model.Specialty;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyMapper {

    public SpecialtyDTO toSpecialtyDTO(Specialty specialty) {
        return new SpecialtyDTO(specialty.getId().toString(), specialty.getName(), specialty.getDepartment().getId().toString(), specialty.getDepartment().getName());
    }
}
