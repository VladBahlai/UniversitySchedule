package com.github.vladbahlai.university.dto;

import java.util.Objects;

public class SpecialtyDTO {

    private String id;
    private String name;
    private String disciplineId;
    private String disciplineName;

    public SpecialtyDTO(String id, String name, String disciplineId, String disciplineName) {
        this.id = id;
        this.name = name;
        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(String disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialtyDTO that = (SpecialtyDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(disciplineId, that.disciplineId) && Objects.equals(disciplineName, that.disciplineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, disciplineId, disciplineName);
    }

    @Override
    public String toString() {
        return "SpecialtyDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", disciplineId='" + disciplineId + '\'' +
                ", disciplineName='" + disciplineName + '\'' +
                '}';
    }
}
