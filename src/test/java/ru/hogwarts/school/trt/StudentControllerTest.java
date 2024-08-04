package ru.hogwarts.school.trt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private final Faker faker = new Faker();

    @AfterEach
    public void afterEach() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @BeforeEach
    public void beforeEach() {
        Faculty faculty1 = generateFaculty();
        Faculty faculty2 = generateFaculty();

        Student student1 = generateStudents();
        Student student2 = generateStudents();
    }

    private Faculty generateFaculty() { // создание факультета
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return facultyRepository.save(faculty);
    }

    private Student generateStudents() { // создание студента без факультета
        Student student = new Student();
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(10, 30));
        return student;
    }

    private String baseUrl(String url) { // базовый URL
        return "http://localhost:%d%s".formatted(port, url);
    }

    private void createStudent(Student student) { //алгоритм создания теста для студента
        ResponseEntity<Student> responseEntity = testRestTemplate.postForEntity(
                baseUrl("/student"),
                student,
                Student.class
        );
        Student actual = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
        assertThat(actual.getId()).isNotNull();

        Optional<Student> fromDb = studentRepository.findById(actual.getId());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(actual);
    }


    @Test
    @DisplayName("Создание студента без указания факультета")
    public void createTest_1() {
        Student student = new Student();
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(10, 30));

        createStudent(student);
    }

    @Test
    @DisplayName("Создание студента с указанием факультетом")
    public void createTest_2() {
        Student student = new Student();
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(10, 30));
        student.setFaculty(facultyRepository.findAll(PageRequest.of(faker.random().nextInt(0, 1), 1)).getContent().get(0));

        createStudent(student);
    }

    @Test
    @DisplayName("Изменение данных студента")
    public void updateTest() {
        int age = new Random().nextInt(10, 30);
        String name = faker.name().firstName();

        Student expected = generateStudents();
        expected = studentRepository.save(expected);

        expected.setAge(age);
        expected.setName(name);

        testRestTemplate.put(baseUrl("/student/") + expected.getId(), expected);

        Student actual = studentRepository.findById(expected.getId()).orElseThrow();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getAge()).isEqualTo(age);
    }

    @Test
    @DisplayName("Поиск студента")
    public void getTest() {
        int age = new Random().nextInt(10, 30);
        String name = faker.name().firstName();

        Student expected = generateStudents();
        expected = studentRepository.save(expected);
        expected.setAge(age);
        expected.setName(name);

        ResponseEntity<Student> response = testRestTemplate.getForEntity(baseUrl("/student/") + expected.getId(),
                Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Удаление студента")
    public void removeTest() {
        int age = new Random().nextInt(10, 30);
        String name = faker.name().firstName();

        Student expected = generateStudents();
        expected = studentRepository.save(expected);
        expected.setAge(age);
        expected.setName(name);

        ResponseEntity<Student> response = testRestTemplate.exchange(baseUrl("/student/") + expected.getId(),
                HttpMethod.DELETE,
                null,
                Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Поиск студентов по возрасту")
    public void getStudentByAgeTest() {
        int age = new Random().nextInt(10, 30);
        String name = faker.name().firstName();

        Student expected = generateStudents();
        expected = studentRepository.save(expected);
        expected.setAge(age);
        expected.setName(name);

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(baseUrl("/student?age=") + expected.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                });

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    @DisplayName("Поиск студентов по минимальному и максимальному возрастам")
    public void findByAgeBetweenTest() {
        Student student = generateStudents();
        student.setAge(15);
        studentRepository.save(student);
        int minAge = faker.random().nextInt(10, 18);
        int maxAge = faker.random().nextInt(minAge, 18);

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                baseUrl("/student?minAge={minAge}&maxAge={maxAge}"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                Map.of("minAge", minAge, "maxAge", maxAge)
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}