package com.github.vladbahlai.university;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.misc.*;
import com.github.vladbahlai.university.model.Student;
import com.github.vladbahlai.university.model.Teacher;
import com.github.vladbahlai.university.model.User;
import com.github.vladbahlai.university.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

@Profile("!test")
@Component
public class UniversityApplicationRunner implements ApplicationRunner {

    private final SpecialtyService specialtyService;
    private final GroupGenerator groupGenerator;
    private final AudienceGenerator audienceGenerator;
    private final TimeSpanGenerator timeSpanGenerator;
    private final StudentGenerator studentGenerator;
    private final GroupService groupService;
    private final DepartmentService departmentService;
    private final TeacherService teacherService;
    private final DisciplineService disciplineService;
    private final DisciplineGenerator disciplineGenerator;
    private final TeacherGenerator teacherGenerator;
    private final LessonGenerator lessonGenerator;
    private final AudienceService audienceService;
    private final TimeSpanService timeSpanService;
    private final UserService userService;
    private final RoleService roleService;
    private final StudentService studentService;
    Logger logger = LoggerFactory.getLogger(UniversityApplicationRunner.class);

    public UniversityApplicationRunner(SpecialtyService specialtyService, GroupGenerator groupGenerator, AudienceGenerator audienceGenerator, TimeSpanGenerator timeSpanGenerator, StudentGenerator studentGenerator, GroupService groupService, DepartmentService departmentService, TeacherService teacherService, DisciplineService disciplineService, DisciplineGenerator disciplineGenerator, TeacherGenerator teacherGenerator, LessonGenerator lessonGenerator, AudienceService audienceService, TimeSpanService timeSpanService, UserService userService, RoleService roleService, StudentService studentService) {
        this.specialtyService = specialtyService;
        this.groupGenerator = groupGenerator;
        this.audienceGenerator = audienceGenerator;
        this.timeSpanGenerator = timeSpanGenerator;
        this.studentGenerator = studentGenerator;
        this.groupService = groupService;
        this.departmentService = departmentService;
        this.teacherService = teacherService;
        this.disciplineService = disciplineService;
        this.disciplineGenerator = disciplineGenerator;
        this.teacherGenerator = teacherGenerator;
        this.lessonGenerator = lessonGenerator;
        this.audienceService = audienceService;
        this.timeSpanService = timeSpanService;
        this.userService = userService;
        this.roleService = roleService;
        this.studentService = studentService;
    }

    @Override
    public void run(ApplicationArguments args) throws UniqueConstraintException {
        if (groupService.getGroups().isEmpty() && timeSpanService.getTimeSpans().isEmpty() && audienceService.getAudiences().isEmpty()) {
            userService.saveUser(new User("admin", "admin", "admin@example.com", Collections.singleton(roleService.getRoleById(1L))));
            userService.saveUser(new User("staff", "staff", "staff@example.com", Collections.singleton(roleService.getRoleById(2L))));
            userService.saveUser(new User("fakeTeacher", "teacher", "fakeTeacher@example.com", Collections.singleton(roleService.getRoleById(3L))));
            userService.saveUser(new User("fakeStudent", "student", "fakeStudent@example.com", Collections.singleton(roleService.getRoleById(4L))));
            logger.info("Created default users.");

            audienceGenerator.generateAudienceData(50, 100, 400);
            timeSpanGenerator.generateTimeSpanData(6, LocalTime.of(8, 0), Duration.ofMinutes(90L), Duration.ofMinutes(20L));
            groupGenerator.generateGroupData(specialtyService.getSpecialties(), 3);
            studentGenerator.generateStudentData(groupService.getGroups(), 5, 15);
            disciplineGenerator.generateDisciplineData(specialtyService.getSpecialties(), 7);
            teacherGenerator.generateTeacherData(departmentService.getDepartments(), 17);
            teacherGenerator.generateTeacherDisciplines(teacherService.getTeachers(), disciplineService.getDisciplines(), 8);
            lessonGenerator.generateLessonDate(LocalDate.now(), 30);

            Teacher teacher = teacherService.getTeacherById(teacherService.getTeachers().get(2).getId());
            teacher.setEmail("teacher@example.com");
            teacher.setPasswordHash("teacher@example.com");
            teacherService.saveTeacher(teacher);
            Student student = studentService.getStudentById(10L);
            student.setEmail("student@example.com");
            student.setPasswordHash("student");
            studentService.saveStudent(student);
            logger.info("Generated random data.");
        }
    }
}
