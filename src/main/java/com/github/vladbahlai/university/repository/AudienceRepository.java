package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.model.TimeSpan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AudienceRepository extends JpaRepository<Audience, Long> {

    @Query(value = "select a from Audience a join Lesson l on a = l.audience where l.date = ?1 and l.timeSpan = ?2 order by a.id")
    List<Audience> findAudiencesByDateAndTimeSpan(LocalDate date, TimeSpan timeSpan);

    boolean existsByName(String name);
}
