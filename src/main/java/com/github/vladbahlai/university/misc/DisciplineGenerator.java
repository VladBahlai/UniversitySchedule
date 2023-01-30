package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.service.DisciplineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.github.vladbahlai.university.utils.RandomGenerator.generateName;
import static com.github.vladbahlai.university.utils.RandomGenerator.getRandomDouble;
import static com.github.vladbahlai.university.utils.Values.DISCIPLINE_NAMES;

@Service
public class DisciplineGenerator {

    private final DisciplineService disciplineService;

    public DisciplineGenerator(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    public void generateDisciplineData(List<Specialty> specialties, int disciplinesInSpecialty) {
        specialties.forEach(specialty -> {
            for (Course course : Course.values()) {
                List<String> names = new ArrayList<>(generateName(disciplinesInSpecialty, DISCIPLINE_NAMES));
                for (String name : names) {
                    double ects =  getRandomDouble(30, 50);
                    disciplineService.saveDiscipline(new Discipline(name, ects, (int) (ects*30), course, specialty));
                }
            }
        });
    }
}
