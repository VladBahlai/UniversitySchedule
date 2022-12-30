package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByName(String name);

}
