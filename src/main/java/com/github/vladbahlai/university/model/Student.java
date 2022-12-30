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

    public Student(String name, String passwordHash) {
        super(name, passwordHash);
    }

    public Student(Long id, String name, String passwordHash) {
        super(id, name, passwordHash);
    }

    public Student(String name, String passwordHash, Group group) {
        super(name, passwordHash);
        this.group = group;
    }

    public Student(Long id, String name, String passwordHash, Group group) {
        super(id, name, passwordHash);
        this.group = group;
    }

    public Student(String name, String passwordHash, Set<Role> roles) {
        super(name, passwordHash, roles);
    }

    public Student(Long id, String name, String passwordHash, Set<Role> roles) {
        super(id, name, passwordHash, roles);
    }

    public Student(String name, String passwordHash, Group group, Set<Role> roles) {
        super(name, passwordHash, roles);
        this.group = group;
    }

    public Student(Long id, String name, String passwordHash, Group group, Set<Role> roles) {
        super(id, name, passwordHash, roles);
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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
        Student other = (Student) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name)
                && Objects.equals(passwordHash, other.passwordHash);
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", passwodHash=" + passwordHash + "]";
    }

}
