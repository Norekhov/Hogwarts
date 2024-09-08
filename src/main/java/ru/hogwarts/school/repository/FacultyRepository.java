package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.List;
/**
*Creating a repository for working with faculties
*/
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
/**
*Repository for obtaining faculty by color
*/
    List<Faculty> getFacultyByColor(String color);
/**
*Repository for obtaining a faculty name or color without taking into account registration
*/
    List<Faculty> getFacultyByColorIgnoreCaseOrNameIgnoreCase(String color, String name);

}
