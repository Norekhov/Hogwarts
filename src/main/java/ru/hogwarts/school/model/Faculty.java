package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
/**
*Creating a Faculty class to work with faculties
*/
@Entity
public class Faculty {
    /**
*Creating Strings for the Faculty Class
*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;


    @OneToMany(mappedBy = "faculty")
    @JsonIgnore
    private List<Student> students;
/**
*Creating a constructor for the Faculty class
*/
    public Faculty(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Faculty() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
/**
*Creating Getters and Setters
*/
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
