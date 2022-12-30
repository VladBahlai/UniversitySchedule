package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.TimeSpan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSpanRepository extends JpaRepository<TimeSpan, Long> {

    boolean existsByNumberOfLesson(Integer numberOfLesson);
}
