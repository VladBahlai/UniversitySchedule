package com.github.vladbahlai.university.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@DiscriminatorValue("T")
public class Teacher extends User {

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @ManyToMany
    @JoinTable(name = "teachers_disciplines", joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "discipline_id", referencedColumnName = "id"))
    private List<Discipline> disciplines = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private List<Lesson> lessons = new ArrayList<>();

    public Teacher() {
    }

    public Teacher(String name, String passwordHash, String email) {
        super(name, passwordHash, email);
    }

    public Teacher(String name, String passwordHash, String email, Department department) {
        super(name, passwordHash, email);
        this.department = department;
    }

    public Teacher(String name, String passwordHash, String email, Set<Role> roles) {
        super(name, passwordHash, email, roles);
    }

    public Teacher(String name, String passwordHash, String email, Department department, Set<Role> roles) {
        super(name, passwordHash, email, roles);
        this.department = department;
    }

    public Teacher(Long id, String name, String passwordHash, String email) {
        super(id, name, passwordHash, email);
    }

    public Teacher(Long id, String name, String passwordHash, String email, Department department) {
        super(id, name, passwordHash, email);
        this.department = department;
    }

    public Teacher(Long id, String name, String passwordHash, String email, Set<Role> roles) {
        super(id, name, passwordHash, email, roles);
    }

    public Teacher(Long id, String name, String passwordHash, String email, Department department, Set<Role> roles) {
        super(id, name, passwordHash, email, roles);
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
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
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(department, teacher.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), department);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "department=" + department +
                ", disciplines=" + disciplines +
                ", lessons=" + lessons +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
