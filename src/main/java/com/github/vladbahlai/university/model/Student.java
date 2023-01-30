package com.github.vladbahlai.university.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.Set;

@Entity
@DiscriminatorValue("S")
public class Student extends User {

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public Student() {
    }

    public Student(String name, String passwordHash, String email) {
        super(name, passwordHash, email);
    }

    public Student(String name, String passwordHash, String email, Group group) {
        super(name, passwordHash, email);
        this.group = group;
    }

    public Student(String name, String passwordHash, String email, Set<Role> roles) {
        super(name, passwordHash, email, roles);
    }

    public Student(String name, String passwordHash, String email, Group group, Set<Role> roles) {
        super(name, passwordHash, email, roles);
        this.group = group;
    }

    public Student(Long id, String name, String passwordHash, String email) {
        super(id, name, passwordHash, email);
    }

    public Student(Long id, String name, String passwordHash, String email, Group group) {
        super(id, name, passwordHash, email);
        this.group = group;
    }

    public Student(Long id, String name, String passwordHash, String email, Set<Role> roles) {
        super(id, name, passwordHash, email, roles);
    }

    public Student(Long id, String name, String passwordHash, String email, Group group, Set<Role> roles) {
        super(id, name, passwordHash, email, roles);
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), group);
    }

    @Override
    public String toString() {
        return "Student{" +
                "group=" + group +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
