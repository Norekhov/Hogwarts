package ru.hogwarts.school.wmt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private  ObjectMapper objectMapper;

    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private AvatarRepository avatarRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private StudentService studentService;
    @SpyBean
    private AvatarService avatarService;
    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private StudentController studentController;

    @Test
    @DisplayName("Сохранение студента")
    void createTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        int age = 22;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);

        Student student = new Student();
        student.setName(name);
        student.setId(id);
        student.setAge(age);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    @DisplayName("Поиск студента по ID")
    void getTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        int age = 22;
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        Student student = new Student(id, name, age);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @DisplayName("Изменение данных студента")
    void updateTest() throws Exception {
        Long id = 1L;
        String newName = "Новое имя";
        int newAge = 22;

        Student student = new Student();
        student.setAge(20);
        student.setName("Имя");
        student.setId(2L);

        Student newStudent = new Student();
        newStudent.setAge(newAge);
        newStudent.setName(newName);
        newStudent.setId(id);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(newStudent)))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Удаление студента")
    void removeTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        int age = 22;
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        Student student = new Student(id, name, age);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", id)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Поиск студентов по минимальному и максимальному возрастам")
    void betweenAgeGetTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        int age = 22;

        int minAge = 10;
        int maxAge = 30;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        Student student = new Student(id, name, age);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findByAgeBetween(1, 5)).thenReturn(List.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?minAge={minAge}&maxAge={maxAge}", minAge, maxAge)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Поиск студентов по возрасту")
    void getStudentByAgeTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        int age = 22;
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);
        Student student = new Student(id, name, age);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findAll()).thenReturn(List.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?age=" + age)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Поиск факультета по ID студента")
    void findFacultyByStudentGetTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        int age = 22;
        Faculty faculty = new Faculty(null, "math", "red");
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);
        Student student = new Student(id, name, age);
        student.setFaculty(faculty);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any())).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}/faculty", id)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("math"))
                .andExpect(jsonPath("$.color").value("red"));
    }
}
