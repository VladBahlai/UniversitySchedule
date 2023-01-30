package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.model.Specialty;
import com.github.vladbahlai.university.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.vladbahlai.university.utils.RandomGenerator.randomChar;
import static com.github.vladbahlai.university.utils.Values.ALPHABET;

@Service
public class GroupGenerator {

    private final GroupService groupService;

    public GroupGenerator(GroupService groupService) {
        this.groupService = groupService;
    }

    public void generateGroupData(List<Specialty> specialties, int countGroupsOnCourse) {
        specialties.forEach(specialty -> {
            StringBuilder abbreviation = new StringBuilder(generateAbbreviation());
            for (Course course : Course.values()) {
                StringBuilder abbreviationWithCourse = new StringBuilder(abbreviation).append(course.ordinal() + 1);
                for (int i = 1; i <= countGroupsOnCourse; i++) {
                    try {
                        groupService.saveGroup(new Group(String.valueOf(abbreviationWithCourse) + i, course, specialty));
                    } catch (UniqueConstraintException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private StringBuilder generateAbbreviation() {
        return new StringBuilder(String.valueOf(randomChar(ALPHABET))).append('-');
    }
}
