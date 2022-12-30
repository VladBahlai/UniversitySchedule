package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.enums.Course;
import com.github.vladbahlai.university.enums.TypeOfLesson;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.service.*;
import com.github.vladbahlai.university.utils.RandomGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonGenerator {

    private final LessonService lessonService;
    private final AudienceService audienceService;
    private final GroupService groupService;
    private final TimeSpanService timeSpanService;
    private final TeacherService teacherService;

    public LessonGenerator(LessonService lessonService, AudienceService audienceService, GroupService groupService, TimeSpanService timeSpanService, TeacherService teacherService) {
        this.lessonService = lessonService;
        this.audienceService = audienceService;
        this.groupService = groupService;
        this.timeSpanService = timeSpanService;
        this.teacherService = teacherService;
    }

    @Transactional
    public void generateLessonDate(LocalDate startDate, int days) {
        for (int i = 0; i < days; i++) {
            if (!isWeekend(startDate)) {
                for (TimeSpan timeSpan : timeSpanService.getTimeSpans()) {
                    List<Teacher> teachers = teacherService.getTeachers();
                    List<Audience> audiences = audienceService.getAudiences();
                    for (Group group : groupService.getGroups()) {
                        if (RandomGenerator.getRandomBoolean()) {
                            Audience audience = RandomGenerator.getRandomListValue(audiences);
                            Teacher teacher = RandomGenerator.getRandomListValue(teachers);
                            if (saveRandomLesson(timeSpan, group, teacher, startDate, TypeOfLesson.values()[RandomGenerator.getRandomInt(0, 4)], audience)) {
                                audiences.remove(audience);
                                teachers.remove(teacher);
                            }
                        }
                    }
                }

            }
            startDate = startDate.plusDays(1);
        }

    }

    private boolean isWeekend(LocalDate dateTime) {
        return dateTime.getDayOfWeek().getValue() > 5;
    }

    private List<Discipline> getCourseDiscipline(Teacher teacher, Course course) {
        return teacher.getDisciplines().stream().filter(discipline -> discipline.getCourse().equals(course)).collect(Collectors.toList());
    }

    private boolean saveRandomLesson(TimeSpan timeSpan, Group group, Teacher teacher, LocalDate date, TypeOfLesson type, Audience audience) {
        List<Discipline> disciplines = getCourseDiscipline(teacher, group.getCourse());
        if (!disciplines.isEmpty()) {
            Discipline discipline = RandomGenerator.getRandomListValue(disciplines);
            lessonService.saveLesson(new Lesson(discipline, group, teacher, date, timeSpan, audience, type));
            return true;
        }
        return false;
    }
}
