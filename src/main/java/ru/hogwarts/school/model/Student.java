package ru.hogwarts.school.model;

import jakarta.persistence.*;


import java.util.Objects;
/**
*Creating a Student class to work with students
*/
@Entity
public class Student {
/**
*Creating Strings for the Student Class
*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
/**
*Creating a constructor for the Student class
*/
    public Student(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ",\"name\":" +"\"" + name + "\"" +
                ",\"age\":" + age +
                '}';
    }

    public Student() {
    }
/**
*Creating Getters and Setters
*/
    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
