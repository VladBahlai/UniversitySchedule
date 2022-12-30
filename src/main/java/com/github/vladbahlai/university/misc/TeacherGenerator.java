package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Department;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Role;
import com.github.vladbahlai.university.model.Teacher;
import com.github.vladbahlai.university.service.RoleService;
import com.github.vladbahlai.university.service.TeacherService;
import com.github.vladbahlai.university.utils.RandomGenerator;
import com.github.vladbahlai.university.utils.Values;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class TeacherGenerator {

    private final TeacherService teacherService;
    private final RoleService roleService;

    public TeacherGenerator(TeacherService teacherService, RoleService roleService) {
        this.teacherService = teacherService;
        this.roleService = roleService;
    }

    public void generateTeacherData(List<Department> departments, int teachersInDepartment) throws UniqueNameConstraintException {
        Set<Role> teacherRoles = new HashSet<>(Arrays.asList(roleService.getRoleByName("ROLE_TEACHER")));
        if (!departments.isEmpty()) {
            List<String> names = new ArrayList<>(RandomGenerator.generateName(departments.size() * teachersInDepartment, Values.FIRST_NAMES, Values.LAST_NAMES));
            int counter = 0;
            for (Department department : departments) {
                for (int i = 0; i < teachersInDepartment; i++) {
                    teacherService.saveTeacher(new Teacher(names.get(counter), RandomGenerator.generatePassword(10), department, teacherRoles));
                    counter++;
                }
            }
        }
    }

    public void generateTeacherDisciplines(List<Teacher> teachers, List<Discipline> disciplines, int numberOfDisciplines) {
        if (!disciplines.isEmpty() && !teachers.isEmpty()) {
            teachers.forEach(teacher -> {
                Set<Discipline> disciplineSet = getRandomDisciplineSet(getDisciplinesByDepartment(disciplines, teacher), numberOfDisciplines);
                for (Discipline discipline : disciplineSet) {
                    teacherService.addDisciplineToTeacher(teacher.getId(), discipline.getId());
                }
            });
        }
    }

    private List<Discipline> getDisciplinesByDepartment(List<Discipline> disciplines, Teacher teacher) {
        return disciplines.stream()
                .filter(discipline ->
                        teacher.getDepartment().equals(discipline.getSpecialty().getDepartment()))
                .collect(Collectors.toList());
    }

    private Set<Discipline> getRandomDisciplineSet(List<Discipline> disciplines, int count) {
        int randomInt = RandomGenerator.getRandomInt(1, count);
        Set<Discipline> disciplineSet = new HashSet<>();
        if (!disciplines.isEmpty()) {
            while (disciplineSet.size() != randomInt) {
                disciplineSet.add(disciplines.get(ThreadLocalRandom.current().nextInt(disciplines.size())));
            }
        }
        return disciplineSet;
    }
}
