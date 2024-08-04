package ru.hogwarts.school.wmt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("id", id);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    @DisplayName("Поиск факультета по ID")
    void getTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        String color = "Цвет";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @DisplayName("Изменение данных факультета")
    public void updateTest() throws Exception {
        long id = 1;

        Faculty expected = new Faculty();
        expected.setId(id);
        expected.setName("Имя1");
        expected.setColor("Цвет1");

        Faculty actual = new Faculty();
        actual.setId(id);
        actual.setName("Имя2");
        actual.setColor("Цвет2");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(expected));
        when(facultyRepository.save(any())).thenReturn(actual);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/faculty/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(actual)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Удаление факультета")
    void removeTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        String color = "Цвет";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findAll()).thenReturn(List.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=" + color)
                        .content(facultyRepository.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Поиск факультета по цвету или имени")
    void getFacultyByColorOrNameTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        String color = "Цвет";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findAll()).thenReturn(List.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?colorOrName=" + color + name)
                        .content(facultyRepository.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Поиск студентов по ID факультета")
    void findStudentsByFacultyIdTest() throws Exception {
        Faculty faculty1 = (new Faculty(1L, "math", "red"));
        Student student1 = (new Student(1L, "roy", 20));
        Student student2 = (new Student(2L, "rob", 25));
        faculty1.setStudents(List.of(student1,student2));
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", faculty1.getName());
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty1);
        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}