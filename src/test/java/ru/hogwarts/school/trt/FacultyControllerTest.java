package ru.hogwarts.school.trt;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

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

    @Test
    @DisplayName("Поиск факультета по ID")
    public void getTest() {
        String color = faker.color().name();
        String name = faker.name().firstName();

        Faculty expected = generateFaculty();
        expected = facultyRepository.save(expected);
        expected.setColor(color);
        expected.setName(name);

        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(baseUrl("/faculty/") + expected.getId(),
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    private void createFaculty(Faculty faculty) { //алгоритм создания теста для факультета
        ResponseEntity<Faculty> responseEntity = testRestTemplate.postForEntity(
                baseUrl("/faculty"),
                faculty,
                Faculty.class
        );
        Faculty actual = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
        assertThat(actual.getId()).isNotNull();

        Optional<Faculty> fromDb = facultyRepository.findById(actual.getId());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(actual);
    }

    @Test
    @DisplayName("Создание факультета")
    public void createTest() {
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().character());
        faculty.setColor(faker.name().firstName());

        createFaculty(faculty);
    }

    @Test
    @DisplayName("Изменение данных факультета")
    public void updateTest() {
        String color = faker.color().name();
        String name = faker.name().firstName();

        Faculty expected = generateFaculty();
        expected = facultyRepository.save(expected);
        expected.setColor(color);
        expected.setName(name);

        testRestTemplate.put(baseUrl("/faculty/") + expected.getId(), expected);

        Faculty actual = facultyRepository.findById(expected.getId()).orElseThrow();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("Удаление факультета")
    public void removeTest() {
        String color = faker.color().name();
        String name = faker.name().firstName();

        Faculty expected = generateFaculty();
        expected = facultyRepository.save(expected);
        expected.setColor(color);
        expected.setName(name);

        ResponseEntity<Faculty> response = testRestTemplate.exchange(baseUrl("/faculty/") + expected.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Поиск факультета по цвету")
    public void getFacultyByColorTest() {
        String color = faker.color().name();
        String name = faker.name().firstName();

        Faculty expected = generateFaculty();
        expected = facultyRepository.save(expected);
        expected.setColor(color);
        expected.setName(name);

        ResponseEntity<Collection<Faculty>> response = testRestTemplate.exchange(baseUrl("/faculty?color=") + expected.getColor(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        Collection<Faculty> faculties = response.getBody();
        assertThat(faculties).isNotNull();
    }

    @Test
    @DisplayName("Поиск факультета по цвету или имени")
    public void getFacultyByColorOrNameTest() {
        String color = faker.color().name();
        String name = faker.name().firstName();

        Faculty expected = generateFaculty();
        expected = facultyRepository.save(expected);
        expected.setColor(color);
        expected.setName(name);

        ResponseEntity<Collection<Faculty>> response = testRestTemplate.exchange
                (baseUrl("/faculty?colorOrName=") + expected.getColor() + expected.getName(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        Collection<Faculty> faculties = response.getBody();
        assertThat(faculties).isNotNull();
    }

    @Test
    @DisplayName("Поиск студентов по ID факультета")
    public void findStudentsByFacultyIdTest() {
        String color = faker.color().name();
        String name = faker.name().firstName();

        Faculty expected = generateFaculty();
        expected = facultyRepository.save(expected);
        expected.setColor(color);
        expected.setName(name);

        ResponseEntity<String> response = testRestTemplate.getForEntity
                (baseUrl("/faculty/") + expected.getId() + ("/students"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

}
