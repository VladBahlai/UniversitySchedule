package com.github.vladbahlai.university.model;

import com.github.vladbahlai.university.enums.TypeOfLesson;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_lesson")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "time_span_id", referencedColumnName = "id")
    private TimeSpan timeSpan;

    @ManyToOne
    @JoinColumn(name = "audience_id", referencedColumnName = "id")
    private Audience audience;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_lesson")
    private TypeOfLesson type;

    public Lesson() {
    }

    public Lesson(Discipline discipline, Group group, Teacher teacher, LocalDate date, TimeSpan timeSpan,
                  Audience audience, TypeOfLesson type) {
        this.discipline = discipline;
        this.group = group;
        this.teacher = teacher;
        this.date = date;
        this.timeSpan = timeSpan;
        this.audience = audience;
        this.type = type;
    }

    public Lesson(Long id, Discipline discipline, Group group, Teacher teacher, LocalDate date, TimeSpan timeSpan,
                  Audience audience, TypeOfLesson type) {
        this.id = id;
        this.discipline = discipline;
        this.group = group;
        this.teacher = teacher;
        this.date = date;
        this.timeSpan = timeSpan;
        this.audience = audience;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TimeSpan getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(TimeSpan timeSpan) {
        this.timeSpan = timeSpan;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public TypeOfLesson getType() {
        return type;
    }

    public void setType(TypeOfLesson type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(audience, date, discipline, group, id, teacher, timeSpan, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lesson other = (Lesson) obj;
        return Objects.equals(audience, other.audience) && Objects.equals(date, other.date)
                && Objects.equals(discipline, other.discipline) && Objects.equals(group, other.group)
                && Objects.equals(id, other.id) && Objects.equals(teacher, other.teacher)
                && Objects.equals(timeSpan, other.timeSpan) && type == other.type;
    }

    @Override
    public String toString() {
        return "Lesson [id=" + id + ", discipline=" + discipline + ", group=" + group + ", teacher=" + teacher
                + ", date=" + date + ", timeSpan=" + timeSpan + ", audience=" + audience + ", type=" + type+ "]";
    }

}
