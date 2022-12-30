package com.github.vladbahlai.university.dto;

import java.util.Objects;

public class CalendarDTO {

    private String title;
    private String start;
    private String end;
    private String description;

    public CalendarDTO(String title, String start, String end, String description) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarDTO that = (CalendarDTO) o;
        return Objects.equals(title, that.title) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, start, end, description);
    }

    @Override
    public String toString() {
        return "CalendarDTO{" +
                "title='" + title + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
