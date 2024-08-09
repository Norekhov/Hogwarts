package ru.hogwarts.school.trt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

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

    private String baseUrl(String url) { // базовый URL
        return "http://localhost:%d%s".formatted(port, url);
    }

    @Test
    @DisplayName("Поиск факультета по ID")
    public void getTest() {
        String color = faker.color().name();
        String name = faker.name().firstName();

        Faculty expected = new Faculty();
        expected.setColor(color);
        expected.setName(name);
        expected = facultyRepository.save(expected);

        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(baseUrl("/faculty/") + expected.getId(),
                Faculty.class);

        Faculty actual = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(expected.getName().equals(actual.getName())).isTrue();
        assertThat(expected.getColor().equals(actual.getColor())).isTrue();
        assertThat(expected.getId().equals(actual.getId())).isTrue();
    }


    @Test
    @DisplayName("Создание факультета")
    public void createTest() {
        String color = faker.color().name();
        String name = faker.name().firstName();

        Faculty expected = new Faculty();
        expected.setColor(color);
        expected.setName(name);
        expected = facultyRepository.save(expected);

        ResponseEntity<Faculty> newResponseEntity = testRestTemplate.postForEntity(
                baseUrl("/faculty"),
                expected,
                Faculty.class);
        assertThat(newResponseEntity.getStatusCode().equals(HttpStatus.OK)).isTrue();
        Faculty newFaculty = newResponseEntity.getBody();

        ResponseEntity<Faculty> responseEntity = testRestTemplate.getForEntity(
                baseUrl("/faculty/") + newFaculty.getId(),
                Faculty.class);

        Faculty faculty = responseEntity.getBody();
        assertThat(faculty.getId().equals(newFaculty.getId())).isTrue();
        assertThat(faculty.getName().equals(newFaculty.getName())).isTrue();
        assertThat(faculty.getColor().equals(newFaculty.getColor())).isTrue();
    }

    @Test
    @DisplayName("Изменение данных факультета")
    public void updateTest() {
        String color = faker.color().name();
        String name = faker.name().firstName();

        Faculty expected = new Faculty();
        expected.setColor(color);
        expected.setName(name);
        expected = facultyRepository.save(expected);

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

        Faculty expected = new Faculty();
        expected = facultyRepository.save(expected);
        expected.setColor(color);
        expected.setName(name);

        ResponseEntity<Faculty> response = testRestTemplate.exchange(baseUrl("/faculty/") + expected.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Faculty> responseGet = testRestTemplate.getForEntity(baseUrl("/faculty/") + expected.getId(),
                Faculty.class);

        assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Поиск факультета по цвету")
    public void getFacultyByColorTest() {
        Faculty faculty1 = new Faculty(1L, "name1", "color1");
        faculty1 = facultyRepository.save(faculty1);
        Faculty faculty2 = new Faculty(2L, "name2", "color2");
        faculty2 = facultyRepository.save(faculty2);
        Faculty faculty3 = new Faculty(3L, "name3", "color3");
        faculty3 = facultyRepository.save(faculty3);

        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange(baseUrl("/faculty?color=color1"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        List<Faculty> faculties = response.getBody();
        assertThat(response.getStatusCode().equals(HttpStatus.OK)).isTrue();
        assertThat(faculties.size() == 1).isTrue();
        assertThat(faculties.get(0).getColor().equals(faculty1.getColor())).isTrue();
    }

    @Test
    @DisplayName("Поиск факультета по цвету или имени")
    public void getFacultyByColorOrNameTest() {
        Faculty faculty1 = new Faculty(1L, "name1", "color1");
        faculty1 = facultyRepository.save(faculty1);
        Faculty faculty2 = new Faculty(2L, "name2", "color2");
        faculty2 = facultyRepository.save(faculty2);
        Faculty faculty3 = new Faculty(3L, "name3", "color3");
        faculty3 = facultyRepository.save(faculty3);

        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange
                (baseUrl("/faculty?color=color3&name=name3"),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        List<Faculty> faculties = response.getBody();
        assertThat(response.getStatusCode().equals(HttpStatus.OK)).isTrue();
        assertThat(faculties.size() == 1).isTrue();
        assertThat(faculties.get(0).getName().equals(faculty3.getName())).isTrue();
        assertThat(faculties.get(0).getColor().equals(faculty3.getColor())).isTrue();
    }

    @Test
    @DisplayName("Поиск студентов по ID факультета")
    public void findStudentsByFacultyIdTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Faculty faculty1 = new Faculty(1L, "name1", "color1");
        faculty1 = facultyRepository.save(faculty1);
        Faculty faculty2 = new Faculty(2L, "name2", "color2");
        faculty2 = facultyRepository.save(faculty2);
        Faculty faculty3 = new Faculty(3L, "name3", "color3");
        faculty3 = facultyRepository.save(faculty3);

        Student student1 = new Student(1l, "max", 20);
        Student student2 = new Student(2l, "alex", 19);
        Student student3 = new Student(3l, "john", 21);

        student1.setFaculty(faculty1);
        student2.setFaculty(faculty2);
        student3.setFaculty(faculty3);

        ResponseEntity<String> response = testRestTemplate.getForEntity
                (baseUrl("/faculty/2/students"), String.class);

        List<Student> students = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(students.size() == 1);
    }

}
