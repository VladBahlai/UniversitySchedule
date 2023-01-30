package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Student;
import com.github.vladbahlai.university.repository.StudentRepository;
import com.github.vladbahlai.university.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String STUDENT_NOT_FOUND = "Student not found with id - ";
    private final StudentRepository repo;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getStudents() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException(STUDENT_NOT_FOUND + id));
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public Student saveStudent(Student student) throws UniqueConstraintException {
        if (student.getId() != null) {
            Student studentDB = repo.findById(student.getId()).orElseThrow(() -> new IllegalArgumentException(STUDENT_NOT_FOUND + student.getId()));
            if (!studentDB.getPasswordHash().equals(student.getPasswordHash())) {
                student.setPasswordHash(passwordEncoder.encode(student.getPasswordHash()));
            }
        } else {
            student.setPasswordHash(passwordEncoder.encode(student.getPasswordHash()));
        }

        if ((repo.existsByEmail(student.getEmail()) && student.getId() == null) ||
                (student.getId() != null &&
                        repo.existsByEmail(student.getEmail()) &&
                        !student.getEmail().equals(repo.findById(student.getId()).orElseThrow(() -> new IllegalArgumentException(STUDENT_NOT_FOUND + student.getId())).getEmail()))) {
            throw new UniqueConstraintException("Student with " + student.getEmail() + " email already exist.");
        }
        return repo.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Student> getPage(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }

}
