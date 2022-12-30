package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.enums.TypeOfLesson;
import com.github.vladbahlai.university.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class LessonRepositoryTest {

    @Autowired
    LessonRepository lessonRepo;

    @Autowired
    GroupRepository groupRepo;

    @Autowired
    TeacherRepository teacherRepo;

    @Autowired
    DisciplineRepository disciplineRepo;

    @Autowired
    TimeSpanRepository timeSpanRepo;

    @Autowired
    AudienceRepository audienceRepo;

    @Autowired
    SpecialtyRepository specialtyRepo;

    @Test
    void shouldReadAndWriteLesson() {
        Specialty specialty = specialtyRepo.save(new Specialty("test"));
        Discipline discipline = disciplineRepo.save(new Discipline("math", 2.0, 15, Course.THIRD, specialty));
        Group group = groupRepo.save(new Group("PA-32", Course.THIRD, specialty));
        Teacher teacher = teacherRepo.save(new Teacher("teacher", "1234"));
        TimeSpan timeSpan = timeSpanRepo.save(new TimeSpan(1, LocalTime.of(10, 43, 12), LocalTime.of(10, 45, 12)));
        Audience audience = audienceRepo.save(new Audience("test"));

        Lesson expected = lessonRepo.save(new Lesson(discipline, group, teacher, LocalDate.of(2020, 1, 8), timeSpan,
                audience, TypeOfLesson.PRACTICE));
        Optional<Lesson> actual = lessonRepo.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

    }

}
