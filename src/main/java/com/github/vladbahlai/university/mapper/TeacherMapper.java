package com.github.vladbahlai.university.mapper;

import com.github.vladbahlai.university.dto.TeacherDTO;
import com.github.vladbahlai.university.model.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public TeacherDTO toTeacherDTO(Teacher teacher) {
        return new TeacherDTO(teacher.getId().toString(), teacher.getName(), teacher.getDepartment().getId().toString(), teacher.getDepartment().getName());
    }
}
