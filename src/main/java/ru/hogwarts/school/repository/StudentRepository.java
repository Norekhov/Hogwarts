package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;
/**
*Creating a repository for working with students
*/
public interface StudentRepository extends JpaRepository<Student, Long> {
/**
*Repository for obtaining a student by age
*/
    List<Student> getStudentByAge(int age);
/**
*Repository for searching students by maximum and minimum age
*/
    List<Student> findByAgeBetween(int minAge, int maxAge);
/**
*Repository for searching students by faculty ID
*/
    List<Student> findStudentsByFaculty_Id(long facultyId);
/**
*SQL query to get the number of all students in a school
*/
    @Query(value = "SELECT count(*) AS count FROM student", nativeQuery = true)
    int getCountStudents();
/**
*SQL query to get average age of students
*/
    @Query(value = "SELECT AVG(age) AS avg FROM student", nativeQuery = true)
    int getAvgAgeStudents();
/**
*SQL query to get the last five students
*/
    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getDescFiveStudents();
/**
*SQL query to get the last five students - parallel mode
*/
    @Query(value = "SELECT name FROM student LIMIT 5", nativeQuery = true)
    List<String> getAllStudentParallelMode();

}
