package ru.hogwarts.school.trt;

import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    private String baseUrl(String url) { // базовый URL
        return "http://localhost:%d%s".formatted(port, url);
    }

    @Test
    @DisplayName("Поиск студента")
    public void getTest() {
        int age = new Random().nextInt(10, 30);
        String name = faker.name().firstName();

        Student expected = new Student();
        expected.setAge(age);
        expected.setName(name);
        expected = studentRepository.save(expected);

        ResponseEntity<Student> response = testRestTemplate.getForEntity(baseUrl("/student/") + expected.getId(),
                Student.class);

        Student actual = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(expected.getId().equals(actual.getId())).isTrue();
        assertThat(expected.getName().equals(actual.getName())).isTrue();
        assertThat(expected.getAge() == (actual.getAge())).isTrue();
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
    @DisplayName("Создание студента")
    public void createTest_1() {
        Student student = new Student();
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(10, 30));

        createStudent(student);
    }

    @Test
    @DisplayName("Изменение данных студента")
    public void updateTest() {
        int age = new Random().nextInt(10, 30);
        String name = faker.name().firstName();

        Student expected = new Student();
        expected.setAge(age);
        expected.setName(name);
        expected = studentRepository.save(expected);

        testRestTemplate.put(baseUrl("/student/") + expected.getId(), expected);

        Student actual = studentRepository.findById(expected.getId()).orElseThrow();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getAge()).isEqualTo(age);
    }

    @Test
    @DisplayName("Удаление студента")
    public void removeTest() {
        int age = new Random().nextInt(10, 30);
        String name = faker.name().firstName();

        Student expected = new Student();
        expected.setAge(age);
        expected.setName(name);
        expected = studentRepository.save(expected);

        ResponseEntity<Student> response = testRestTemplate.exchange(baseUrl("/student/") + expected.getId(),
                HttpMethod.DELETE,
                null,
                Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Student> responseGet = testRestTemplate.getForEntity(baseUrl("/student/") + expected.getId(),
                Student.class);

        assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Поиск студентов по возрасту")
    public void getStudentByAgeTest() {
        Student student1 = new Student(1l, "max", 20);
        student1 = studentRepository.save(student1);
        Student student2 = new Student(2l, "alex", 19);
        student2 = studentRepository.save(student2);
        Student student3 = new Student(3l, "john", 21);
        student3 = studentRepository.save(student3);

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(baseUrl("/student?age=20"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                });

        List<Student> students = response.getBody();
        assertThat(response.getStatusCode().equals(HttpStatus.OK)).isTrue();
        assertThat(students.size() == 1).isTrue();
        assertThat(students.get(0).getAge() == (student1.getAge())).isTrue();
    }

    @Test
    @DisplayName("Поиск студентов по минимальному и максимальному возрастам")
    public void findByAgeBetweenTest() {
        Student student1 = new Student(1l, "max", 20);
        student1 = studentRepository.save(student1);
        Student student2 = new Student(2l, "alex", 19);
        student2 = studentRepository.save(student2);
        Student student3 = new Student(3l, "john", 21);
        student3 = studentRepository.save(student3);

        int minAge = 18;
        int maxAge = 19;

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                baseUrl("/student?minAge={minAge}&maxAge={maxAge}"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                Map.of("minAge", minAge, "maxAge", maxAge)
        );

        List<Student> students = response.getBody();
        assertThat(response.getStatusCode().equals(HttpStatus.OK)).isTrue();
        assertThat(students.size() == 1).isTrue();
    }
}