package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
/**
*Creating a service for working with facultys
*/
@Service
public class FacultyService {
    private FacultyRepository facultyRepository;
    private StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }
/**
*Implementation of the method for creating a faculty
*/
    public Faculty create(Faculty faculty) {
        logger.info("Был вызван метод для \"createFaculty\"");
        faculty.setId(null);
        logger.debug("Был передан \"faculty\"={} в репозиторий из метода \"createFaculty\"", faculty);
        return facultyRepository.save(faculty);
    }
/**
*Implementation of the method for changing the faculty
*/
    public void update(long id, Faculty faculty) {
        logger.info("Был вызван метод для \"updateFaculty\"");
        Faculty oldFaculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Нет факультета с id = {}", id);
                    return new FacultyNotFoundException(id);
                });
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        facultyRepository.save(oldFaculty);
    }
/**
*Implementation of the method for obtaining a faculty
*/
    public Faculty get(long id) {
        logger.info("Был вызван метод для \"getFaculty\"");
        logger.debug("Был запрос \"facultyRepository.findById(id)\"={} в репозитории из метода \"getFaculty\"", id);
        return facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("Нет факультета с id = {}", id);
            return new FacultyNotFoundException(id);
        });
    }
/**
*Implementation of the method for removing a faculty
*/
    public Faculty remove(long id) {
        logger.info("Был вызван метод для \"removeFaculty\"");
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("Нет факультета с id = {}", id);
            return new FacultyNotFoundException(id);
        });
        logger.debug("Был запрос \"facultyRepository.delete(faculty)\"={} " + "в репозитории из метода \"removeFaculty\"", faculty);
        facultyRepository.delete(faculty);
        return faculty;
    }
/**
*Implementation of the method for obtaining a faculty by color
*/
    public List<Faculty> getFacultyByColor(String color) {
        logger.info("Был вызван метод для \"getFacultyByColor\"");
        return facultyRepository.getFacultyByColor(color);
    }
/**
*Implementation of the method for obtaining a faculty by color or name
*/
    public List<Faculty> getFacultyByColorOrName(String colorOrName) {
        logger.info("Был вызван метод для \"getFacultyByColorOrName\"");
        return facultyRepository.getFacultyByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }
/**
*Implementation of the method for searching a student by faculty ID
*/
    public List<Student> findStudentsByFacultyId(long id) {
        logger.info("Был вызван метод для \"findStudentsByFacultyId\"");
        return studentRepository.findStudentsByFaculty_Id(id);
    }
/**
*IImplementation of the method for obtaining the longest faculty name
*/
    public String getTheLongestFacultyName() {
        logger.info("Был вызван метод для \"getTheLongestFacultyName\"");
        return facultyRepository.findAll()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length)).orElseThrow();
    }
/**
*Implementations of the method to get integer value
*/
    public Integer getIntegerValue() {
        logger.info("Был вызван метод для \"getIntegerValue\"");
        return Stream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);
    }
}
