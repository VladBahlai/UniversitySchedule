package com.github.vladbahlai.university.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "time_spans")
public class TimeSpan {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_of_lesson")
    private int numberOfLesson;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Column(name = "start_time")
    private LocalTime startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Column(name = "end_time")
    private LocalTime endTime;

    @OneToMany(mappedBy = "timeSpan")
    private List<Lesson> lessons = new ArrayList<>();

    public TimeSpan() {

    }

    public TimeSpan(int numberOfLesson, LocalTime startTime, LocalTime endTime) {
        this.numberOfLesson = numberOfLesson;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeSpan(Long id, int numberOfLesson, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.numberOfLesson = numberOfLesson;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfLesson() {
        return numberOfLesson;
    }

    public void setNumberOfLesson(int numberOfLesson) {
        this.numberOfLesson = numberOfLesson;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public int hashCode() {
        return Objects.hash(endTime, id, numberOfLesson, startTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimeSpan other = (TimeSpan) obj;
        return Objects.equals(endTime, other.endTime) && Objects.equals(id, other.id)
                && numberOfLesson == other.numberOfLesson && Objects.equals(startTime, other.startTime);
    }

    @Override
    public String toString() {
        return "TimeSpan [id=" + id + ", numberOfLesson=" + numberOfLesson + ", startTime=" + startTime + ", endTime="
                + endTime + "]";
    }

}
