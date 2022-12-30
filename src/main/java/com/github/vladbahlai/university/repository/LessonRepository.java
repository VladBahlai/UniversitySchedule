package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.model.Group;
import com.github.vladbahlai.university.model.Lesson;
import com.github.vladbahlai.university.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByTeacherAndDateBetween(Teacher teacher, LocalDate start, LocalDate end);

    List<Lesson> findByGroupAndDateBetween(Group group, LocalDate start, LocalDate end);

    List<Lesson> findByAudienceAndDateBetween(Audience audience, LocalDate start, LocalDate end);
}
