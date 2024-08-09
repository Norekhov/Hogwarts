package ru.hogwarts.school.wmt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private FacultyService facultyService;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;


    @Test
    @DisplayName("Создание факультета")
    void createTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        String color = "Цвет";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(faculty.getName()).isEqualTo(name);
                    assertThat(faculty.getColor()).isEqualTo(color);
                    assertThat(faculty.getId()).isEqualTo(id);
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });
    }

    @Test
    @DisplayName("Поиск факультета по ID")
    void getTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        String color = "Цвет";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(faculty.getId()).isEqualTo(id);
                    assertThat(faculty.getName()).isEqualTo(name);
                    assertThat(faculty.getColor()).isEqualTo(color);
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });
    }

    @Test
    @DisplayName("Изменение данных факультета")
    public void updateTest() throws Exception {
        long id = 1L;
        String actualName = "Name2";
        String actualColor = "Color2";

        Faculty expected = new Faculty();
        expected.setId(id);
        expected.setName("Name1");
        expected.setColor("Color1");

        Faculty actual = new Faculty();
        actual.setId(id);
        actual.setName(actualName);
        actual.setColor(actualColor);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(expected));
        when(facultyRepository.save(any())).thenReturn(actual);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/{id}", id)
                        .content(objectMapper.writeValueAsString(actual))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(actual.getName()).isEqualTo(actualName);
                    assertThat(actual.getColor()).isEqualTo(actualColor);
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });
    }

    @Test
    @DisplayName("Удаление факультета")
    void removeTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        String color = "Цвет";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(faculty.getId()).isEqualTo(id);
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(faculty.getId()).isEqualTo(id);
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });
    }

    @Test
    @DisplayName("Поиск факультета по цвету")
    void getFacultyByColorTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        String color = "Цвет";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        when(facultyRepository.getFacultyByColor(any(String.class))).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=" + color)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(faculty.getName()).isEqualTo(name);
                    assertThat(faculty.getColor()).isEqualTo(color);
                    assertThat(faculty.getId()).isEqualTo(id);
                });
    }

    @Test
    @DisplayName("Поиск факультета по цвету или имени")
    void getFacultyByColorOrNameTest() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setColor("color1");
        faculty1.setName("name1");
        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setColor("color2");
        faculty2.setName("name2");
        Faculty faculty3 = new Faculty();
        faculty3.setId(3L);
        faculty3.setColor("color3");
        faculty3.setName("name3");

        when(facultyRepository.getFacultyByColorIgnoreCaseOrNameIgnoreCase(any(String.class), any(String.class))).thenReturn(List.of(faculty3));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=color3&name=name3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(faculty3.getName()).isEqualTo("name3");
                    assertThat(faculty3.getColor()).isEqualTo("color3");
                    assertThat(faculty3.getId()).isEqualTo(3L);
                });
    }

    @Test
    @DisplayName("Поиск студентов по ID факультета")
    void findStudentsByFacultyIdTest() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setColor("color1");
        faculty1.setName("name1");
        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setColor("color2");
        faculty2.setName("name2");
        Faculty faculty3 = new Faculty();
        faculty3.setId(3L);
        faculty3.setColor("color3");
        faculty3.setName("name3");

        Student student1 = new Student(1l, "max", 20);
        Student student2 = new Student(2l, "alex", 19);
        Student student3 = new Student(3l, "john", 21);

        student1.setFaculty(faculty1);
        student2.setFaculty(faculty2);
        student3.setFaculty(faculty3);

        when(studentRepository.findStudentsByFaculty_Id(any(Long.class))).thenReturn(List.of(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(student1.getName()).isEqualTo("max");
                    assertThat(student1.getAge()).isEqualTo(20);
                    assertThat(student1.getId()).isEqualTo(1L);
                });
    }
}