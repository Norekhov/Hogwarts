package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
*Creating a service for working with students
*/
@Service
public class StudentService {

    private StudentRepository studentRepository;
    private FacultyRepository facultyRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    final Object flag = new Object();

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }
/**
*Implementation of the method for creating a student
*/
    public Student create(Student student) {
        logger.info("Был вызван метод для \"createStudent\"");
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> {
                        logger.error("Нет факультета с id = {}", student.getFaculty().getId());
                        return new FacultyNotFoundException(student.getFaculty().getId());
                    });
        }
        student.setFaculty(faculty);
        student.setId(null);
        logger.debug("Был передан \"student\"={} в репозиторий из метода \"createStudent\"", student);
        return studentRepository.save(student);
    }
/**
*Implementation of the method for changing the student
*/
    public void update(long id, Student student) {
        logger.info("Был вызван метод для \"updateStudent\"");
        logger.debug("Был запрос \"studentRepository.findById(id)\"={} " + "в репозитории из метода \"updateStudent\"", id);
        Student oldStudent = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Нет студента с id = {}", id);
                    return new StudentNotFoundException(id);
                });
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> {
                        logger.error("Нет факультета с id = {}", student.getFaculty().getId());
                        return new FacultyNotFoundException(student.getFaculty().getId());
                    });
        }
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        oldStudent.setFaculty(faculty);
        studentRepository.save(oldStudent);
    }
/**
*Implementation of the method for obtaining a student
*/
    public Student get(long id) {
        logger.info("Был вызван метод для \"getStudent\"");
        logger.debug("Был запрос \"studentRepository.findById(id)\"={} в репозитории из метода \"getStudent\"", id);
        return studentRepository.findById(id).orElseThrow(() -> {
            logger.error("Нет студента с id = {}", id);
            return new StudentNotFoundException(id);
        });
    }
/**
*Implementation of the method for removing a student
*/
    public Student remove(long id) {
        logger.info("Был вызван метод для \"removeStudent\"");
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            logger.error("Нет студента с id = {}", id);
            return new StudentNotFoundException(id);
        });
        logger.debug("Был запрос \"studentRepository.delete(student)\"={} " + "в репозитории из метода \"removeStudent\"", student);
        studentRepository.delete(student);
        return student;
    }
/**
*Implementation of the method for obtaining all students by age
*/
    public List<Student> getStudentByAge(int age) {
        logger.info("Был вызван метод для \"getStudentByAge\"");
        return studentRepository.getStudentByAge(age);
    }
/**
*Implementation of the method for obtaining students with maximum and minimum ages
*/
    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Был вызван метод для \"findByAgeBetween\"");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }
/**
*Implementation of the method for obtaining students by faculty ID
*/
    private Faculty findFaculty(Long facultyId) {
        logger.info("Был вызван метод для \"findFaculty\"");
        Faculty faculty = null;
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> {
                    logger.error("Нет факультета с id = {}", facultyId);
                    return new FacultyNotFoundException(facultyId);
                });
    }
/**
*Implementation of the method for obtaining students at the faculty
*/
    public Faculty findStudentsByFaculty(long id) {
        logger.info("Был вызван метод для \"findStudentsByFaculty\"");
        logger.debug("Был запрос \"get(id).getFaculty()\"={} " + "в репозиторий из метода \"findStudentsByFaculty\"", id);
        return get(id).getFaculty();
    }
/**
*Implementation of the method for obtaining the number of students
*/
    public long getCountStudents() {
        logger.info("Был вызван метод для \"getCountStudents\"");
        return studentRepository.getCountStudents();
    }
/**
*Implementation of the method for obtaining the average age of students
*/
    public double getAvgAgeStudents() {
        logger.info("Был вызван метод для \"getAvgAgeStudents\"");
        return studentRepository.getAvgAgeStudents();
    }
/**
*Implementation of the method for obtaining the last five students
*/
    public List<Student> getDescFiveStudents() {
        logger.info("Был вызван метод для \"getDescFiveStudents\"");
        return studentRepository.getDescFiveStudents();
    }
/**
*Implementation of the method for obtaining students with a name starting with A
*/
    public List<String> getStudentsWithNameStartingWithA() {
        logger.info("Был вызван метод для \"getStudentsWithNameStartingWithA\"");
        return studentRepository.findAll()
                .stream()
                .parallel()
                .map(Student::getName)
                .filter(n -> n.startsWith("A") & n.startsWith("а"))
                .sorted()
                .collect(Collectors.toList());
    }
/**
*Implementation of the method for obtaining the average age of students
*/
    public double getTheAverageAgeOfStudents() {
        logger.info("Был вызван метод для \"getTheAverageAgeOfStudents\"");
        return studentRepository.findAll()
                .stream()
                .parallel()
                .collect(Collectors.averagingInt(Student::getAge));
    }

    int count = 0;
/**
*Implementation of the method for printing students' names
*/
    void printStudentsNames(List<String> name, int id) {
        if (name.get(id) != null) {
            System.out.println(name.get(id) + ", " + count);
            count++;
        }
    }
/**
*Implementation of the method for obtaining all students using a parallel mode
*/
    public void getAllStudentsParallelMode() {
        logger.info("Был вызван метод для \"getAllStudentParallelMode\"");
        List<String> namesOfStudentsInParallelMode = studentRepository.getAllStudentParallelMode();
        System.out.println(namesOfStudentsInParallelMode);
        printStudentsNames(namesOfStudentsInParallelMode, 0);
        printStudentsNames(namesOfStudentsInParallelMode, 1);

        new Thread(() -> {
            printStudentsNames(namesOfStudentsInParallelMode, 2);
            printStudentsNames(namesOfStudentsInParallelMode, 3);
        }).start();

        new Thread(() -> {
            printStudentsNames(namesOfStudentsInParallelMode, 4);
            printStudentsNames(namesOfStudentsInParallelMode, 5);

        }).start();
    }
/**
*Implementation of the method for obtaining all students using a synchronous mode
*/
    public void getAllStudentsSynchronousMode() {
        logger.info("Был вызван метод для \"getAllStudentParallelMode\"");
        List<String> namesOfStudentsInParallelMode = studentRepository.getAllStudentParallelMode();
        System.out.println(namesOfStudentsInParallelMode);
        printStudentsNames(namesOfStudentsInParallelMode, 0);
        printStudentsNames(namesOfStudentsInParallelMode, 1);

        new Thread(() -> {
            synchronized (flag) {
                printStudentsNames(namesOfStudentsInParallelMode, 2);
                printStudentsNames(namesOfStudentsInParallelMode, 3);
            }
        }).start();

        new Thread(() -> {
            synchronized (flag) {
            printStudentsNames(namesOfStudentsInParallelMode, 4);
            printStudentsNames(namesOfStudentsInParallelMode, 5);
            }
        }).start();
    }
}
