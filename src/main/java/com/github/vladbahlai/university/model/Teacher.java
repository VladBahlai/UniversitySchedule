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

    public Teacher(String name, String passwordHash) {
        super(name, passwordHash);
    }

    public Teacher(Long id, String name, String passwordHash) {
        super(id, name, passwordHash);
    }

    public Teacher(String name, String passwordHash, Department department) {
        super(name, passwordHash);
        this.department = department;
    }

    public Teacher(Long id, String name, String passwordHash, Department department) {
        super(id, name, passwordHash);
        this.department = department;
    }

    public Teacher(String name, String passwordHash, Set<Role> roles) {
        super(name, passwordHash, roles);
    }

    public Teacher(Long id, String name, String passwordHash, Set<Role> roles) {
        super(id, name, passwordHash, roles);
    }

    public Teacher(String name, String passwordHash, Department department, Set<Role> roles) {
        super(name, passwordHash, roles);
        this.department = department;
    }

    public Teacher(Long id, String name, String passwordHash, Department department, Set<Role> roles) {
        super(id, name, passwordHash, roles);
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
    public int hashCode() {
        return Objects.hash(id, name, passwordHash);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Teacher other = (Teacher) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name)
                && Objects.equals(passwordHash, other.passwordHash);
    }

    @Override
    public String toString() {
        return "Teacher [id=" + id + ", name=" + name + ", passwordHash=" + passwordHash + "]";
    }

}
