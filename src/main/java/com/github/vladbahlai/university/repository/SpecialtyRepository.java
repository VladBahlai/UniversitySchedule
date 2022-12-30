package com.github.vladbahlai.university.repository;

import com.github.vladbahlai.university.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {

    boolean existsByName(String name);

}
