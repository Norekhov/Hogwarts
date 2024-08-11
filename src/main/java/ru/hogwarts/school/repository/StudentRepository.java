package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> getStudentByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findStudentsByFaculty_Id(long facultyId);

    @Query(value = "SELECT count(*) AS count FROM student", nativeQuery = true)
    int getCountStudents();

    @Query(value = "SELECT AVG(age) AS avg FROM student", nativeQuery = true)
    int getAvgAgeStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getDescFiveStudents();

}
