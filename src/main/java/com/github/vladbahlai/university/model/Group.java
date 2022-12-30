package com.github.vladbahlai.university.model;

import com.github.vladbahlai.university.enums.Course;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "course")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "id")
    private Specialty specialty;

    @OneToMany(mappedBy = "group")
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Lesson> lessons = new ArrayList<>();

    public Group() {
    }

    public Group(String name, Course course) {
        this.name = name;
        this.course = course;
    }

    public Group(Long id, String name, Course course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }


    public Group(String name, Course course, Specialty specialty) {
        this.name = name;
        this.course = course;
        this.specialty = specialty;
    }

    public Group(Long id, String name, Course course, Specialty specialty) {
        this.id = id;
        this.name = name;
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        return course == other.course && Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + ", course=" + course + "]";
    }

}
