package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Discipline;
import com.github.vladbahlai.university.model.Teacher;
import com.github.vladbahlai.university.repository.DisciplineRepository;
import com.github.vladbahlai.university.repository.TeacherRepository;
import com.github.vladbahlai.university.service.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    private static final String TEACHER_NOT_FOUND = "Teacher not found with id - ";
    private final TeacherRepository teacherRepo;
    private final DisciplineRepository disciplineRepo;
    private final PasswordEncoder passwordEncoder;

    public TeacherServiceImpl(TeacherRepository teacherRepo, DisciplineRepository disciplineRepo, PasswordEncoder passwordEncoder) {
        this.teacherRepo = teacherRepo;
        this.disciplineRepo = disciplineRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> getTeachers() {
        return teacherRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherById(Long id) {
        return teacherRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(TEACHER_NOT_FOUND + id));
    }

    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        teacherRepo.deleteById(id);
    }

    @Override
    @Transactional
    public Teacher saveTeacher(Teacher teacher) throws UniqueConstraintException {
        if (teacher.getId() != null) {
            Teacher teacherDB = teacherRepo.findById(teacher.getId()).orElseThrow(() -> new IllegalArgumentException(TEACHER_NOT_FOUND + teacher.getId()));
            if (!teacherDB.getPasswordHash().equals(teacher.getPasswordHash())) {
                teacher.setPasswordHash(passwordEncoder.encode(teacher.getPasswordHash()));
            }
        } else {
            teacher.setPasswordHash(passwordEncoder.encode(teacher.getPasswordHash()));
        }
        if ((teacherRepo.existsByEmail(teacher.getEmail()) && teacher.getId() == null) ||
                (teacher.getId() != null &&
                        teacherRepo.existsByEmail(teacher.getEmail()) &&
                        !teacher.getEmail().equals(teacherRepo.findById(teacher.getId()).orElseThrow(() -> new IllegalArgumentException(TEACHER_NOT_FOUND + teacher.getId())).getEmail()))) {
            throw new UniqueConstraintException("Teacher with " + teacher.getEmail() + " email already exist.");
        }
        return teacherRepo.save(teacher);
    }

    @Override
    @Transactional
    public Teacher addDisciplineToTeacher(Long teacherId, Long disciplineId) {
        Optional<Teacher> teacher = teacherRepo.findById(teacherId);
        Optional<Discipline> discipline = disciplineRepo.findById(disciplineId);

        if (teacher.isPresent() && discipline.isPresent()) {
            if (!teacher.get().getDisciplines().contains(discipline.get())) {
                teacher.get().getDisciplines().add(discipline.get());
                return teacher.get();
            }
        }
        throw new IllegalArgumentException("Could not add discipline to teacher");
    }

    @Override
    @Transactional
    public Teacher deleteDisciplineFromTeacher(Long teacherId, Long disciplineId) {
        Optional<Teacher> teacher = teacherRepo.findById(teacherId);
        Optional<Discipline> discipline = disciplineRepo.findById(disciplineId);

        if (teacher.isPresent() && discipline.isPresent()) {
            if (teacher.get().getDisciplines().contains(discipline.get())) {
                teacher.get().getDisciplines().remove(discipline.get());
                return teacher.get();
            }
        }
        throw new IllegalArgumentException("Could not remove discipline from teacher");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Teacher> getPage(PageRequest pageRequest) {
        return teacherRepo.findAll(pageRequest);
    }
}
