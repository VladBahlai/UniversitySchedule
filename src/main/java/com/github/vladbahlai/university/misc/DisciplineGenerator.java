package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.service.DisciplineService;
import com.github.vladbahlai.university.utils.RandomGenerator;
import com.github.vladbahlai.university.utils.Values;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DisciplineGenerator {

    private final DisciplineService disciplineService;

    public DisciplineGenerator(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    public void generateDisciplineData(List<Specialty> specialties, int disciplinesInSpecialty) {
        specialties.forEach(specialty -> {
            for (Course course : Course.values()) {
                List<String> names = new ArrayList<>(RandomGenerator.generateName(disciplinesInSpecialty, Values.DISCIPLINE_NAMES));
                for (String name : names) {
                    double ects =  RandomGenerator.getRandomDouble(30, 50);
                    disciplineService.saveDiscipline(new Discipline(name, ects, (int) (ects*30), course, specialty));
                }
            }
        });
    }
}
