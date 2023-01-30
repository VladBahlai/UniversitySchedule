package com.github.vladbahlai.university.service.impl;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.*;
import com.github.vladbahlai.university.repository.StudentRepository;
import com.github.vladbahlai.university.repository.TeacherRepository;
import com.github.vladbahlai.university.repository.UserRepository;
import com.github.vladbahlai.university.security.MyUserDetails;
import com.github.vladbahlai.university.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User not found with id - ";
    private final UserRepository repo;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repo, TeacherRepository teacherRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        Set<Privilege> privileges = new HashSet<>();
        for (Role role : user.getRoles()) {
            privileges.addAll(role.getPrivileges());
        }
        return new MyUserDetails(user, privileges);
    }

    @Override
    @Transactional
    public User saveUser(User user) throws UniqueConstraintException {
        if ((repo.existsByEmail(user.getEmail()) && user.getId() == null) ||
                (user.getId() != null &&
                        repo.existsByEmail(user.getEmail()) &&
                        !user.getEmail().equals(repo.findById(user.getId())
                                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND + user.getId())).getEmail()))) {
            throw new UniqueConstraintException("User with " + user.getEmail() + " email already exist.");
        }
        if (user.getId() != null) {
            if (!repo.findById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND + user.getId())).getPasswordHash().equals(user.getPasswordHash())) {
                user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            }
            if (teacherRepository.existsById(user.getId())) {
                Teacher teacher = teacherRepository.findById(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND + user.getId()));
                teacher.setName(user.getEmail());
                teacher.setPasswordHash(user.getPasswordHash());
                return teacherRepository.save(teacher);
            } else if (studentRepository.existsById(user.getId())) {
                Student student = studentRepository.findById(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND + user.getId()));
                student.setName(user.getEmail());
                student.setPasswordHash(user.getPasswordHash());
                return studentRepository.save(student);
            }
        } else {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }
        return repo.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND + id));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getPage(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }
}
