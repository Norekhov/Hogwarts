package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class FacultyService {
    private FacultyRepository facultyRepository;
    private StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty create(Faculty faculty) {
        logger.info("Был вызван метод для \"createFaculty\"");
        faculty.setId(null);
        logger.debug("Был передан \"faculty\"={} в репозиторий из метода \"createFaculty\"", faculty);
        return facultyRepository.save(faculty);
    }

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

    public Faculty get(long id) {
        logger.info("Был вызван метод для \"getFaculty\"");
        logger.debug("Был запрос \"facultyRepository.findById(id)\"={} в репозитории из метода \"getFaculty\"", id);
        return facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("Нет факультета с id = {}", id);
            return new FacultyNotFoundException(id);
        });
    }

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

    public List<Faculty> getFacultyByColor(String color) {
        logger.info("Был вызван метод для \"getFacultyByColor\"");
        return facultyRepository.getFacultyByColor(color);
    }

    public List<Faculty> getFacultyByColorOrName(String colorOrName) {
        logger.info("Был вызван метод для \"getFacultyByColorOrName\"");
        return facultyRepository.getFacultyByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }

    public List<Student> findStudentsByFacultyId(long id) {
        logger.info("Был вызван метод для \"findStudentsByFacultyId\"");
        return studentRepository.findStudentsByFaculty_Id(id);
    }
}