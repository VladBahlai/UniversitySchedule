package com.github.vladbahlai.university.model;

import com.github.vladbahlai.university.enums.Course;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "disciplines")
public class Discipline {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ects")
    private Double ects;

    @Column(name = "total_clock")
    private Integer totalClock;

    @Enumerated(EnumType.STRING)
    @Column(name = "course")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "id")
    private Specialty specialty;

    @ManyToMany(mappedBy = "disciplines")
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "discipline")
    private List<Lesson> lessons = new ArrayList<>();

    public Discipline() {
    }

    public Discipline(String name, Double ects, Integer totalClock, Course course, Specialty specialty) {
        this.name = name;
        this.ects = ects;
        this.totalClock = totalClock;
        this.course = course;
        this.specialty = specialty;
    }

    public Discipline(Long id, String name, Double ects, Integer totalClock, Course course, Specialty specialty) {
        this.id = id;
        this.name = name;
        this.ects = ects;
        this.totalClock = totalClock;
        this.course = course;
        this.specialty = specialty;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getEcts() {
        return ects;
    }

    public void setEcts(Double ects) {
        this.ects = ects;
    }

    public Integer getTotalClock() {
        return totalClock;
    }

    public void setTotalClock(Integer totalClock) {
        this.totalClock = totalClock;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discipline that = (Discipline) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(ects, that.ects) && Objects.equals(totalClock, that.totalClock) && course == that.course && Objects.equals(specialty, that.specialty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ects, totalClock, course, specialty);
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ects=" + ects +
                ", totalClock=" + totalClock +
                ", course=" + course +
                ", specialty=" + specialty +
                '}';
    }

}
