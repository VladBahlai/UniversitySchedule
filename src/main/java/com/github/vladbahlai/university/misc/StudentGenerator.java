package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.model.Role;
import com.github.vladbahlai.university.model.Student;
import com.github.vladbahlai.university.service.RoleService;
import com.github.vladbahlai.university.service.StudentService;
import com.github.vladbahlai.university.utils.RandomGenerator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class StudentGenerator {

    private final StudentService studentService;
    private final RoleService roleService;

    public StudentGenerator(StudentService studentService, RoleService roleService) {
        this.studentService = studentService;
        this.roleService = roleService;
    }

    public void generateStudentData(List<Group> groups, int minNumberOfStudent, int maxNumberOfStudent) throws UniqueNameConstraintException {
        List<String> names = new ArrayList<>(RandomGenerator.generateFullName(groups.size() * maxNumberOfStudent));
        Set<Role> studentRoles = new HashSet<>(Arrays.asList(roleService.getRoleByName("ROLE_STUDENT")));
        int counter = 0;
        for (Group group : groups) {
            int count = ThreadLocalRandom.current().nextInt(minNumberOfStudent, maxNumberOfStudent);
            for (int i = 0; i < count; i++) {
                studentService.saveStudent(new Student(names.get(counter), RandomGenerator.generatePassword(10), group, studentRoles));
                counter++;
            }
        }

    }
}
