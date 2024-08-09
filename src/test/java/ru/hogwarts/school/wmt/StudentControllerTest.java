package ru.hogwarts.school.wmt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        Student student = new Student();
        student.setName(name);
        student.setId(id);
        student.setAge(age);

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));
        when(studentRepository.save(any())).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(student.getName()).isEqualTo(name);
                    assertThat(student.getAge()).isEqualTo(age);
                    assertThat(student.getId()).isEqualTo(id);
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });
    }

    @Test
    @DisplayName("Поиск студента по ID")
    void getTest() throws Exception {
        Long id = 1L;
        String name = "Имя";
        int age = 22;

        Student student = new Student();
        student.setName(name);
        student.setId(id);
        student.setAge(age);

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(student.getId()).isEqualTo(id);
                    assertThat(student.getName()).isEqualTo(name);
                    assertThat(student.getAge()).isEqualTo(age);
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });
    }

    @Test
    @DisplayName("Изменение данных студента")
    void updateTest() throws Exception {
        long id = 1L;
        String actualName = "Name2";
        int actualAge = 22;

        Student expected = new Student();
        expected.setId(id);
        expected.setName("Name1");
        expected.setAge(20);

        Student actual = new Student();
        actual.setId(id);
        actual.setName(actualName);
        actual.setAge(actualAge);

        when(studentRepository.findById(any())).thenReturn(Optional.of(expected));
        when(studentRepository.save(any())).thenReturn(actual);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/{id}", id)
                        .content(objectMapper.writeValueAsString(actual))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(actual.getName()).isEqualTo(actualName);
                    assertThat(actual.getAge()).isEqualTo(actualAge);
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });
    }

    @Test
    @DisplayName("Удаление студента")
    void removeTest() throws Exception {
        long id = 1L;
        String name = "Name";
        int age = 22;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(student.getId()).isEqualTo(id);
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(student.getId()).isEqualTo(id);
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });
    }


    @Test
    @DisplayName("Поиск студентов по минимальному и максимальному возрастам")
    void betweenAgeGetTest() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setAge(20);
        student1.setName("name1");
        Student student2 = new Student();
        student2.setId(2L);
        student2.setAge(21);
        student2.setName("name2");
        Student student3 = new Student();
        student3.setId(3L);
        student3.setAge(22);
        student3.setName("name3");

        when(studentRepository.findByAgeBetween(any(Integer.class), any(Integer.class))).thenReturn(List.of(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?minAge=18&maxAge=20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(student1.getName()).isEqualTo("name1");
                    assertThat(student1.getAge()).isEqualTo(20);
                    assertThat(student1.getId()).isEqualTo(1L);
                });
    }

    @Test
    @DisplayName("Поиск студентов по возрасту")
    void getStudentByAgeTest() throws Exception {
        long id = 1L;
        String name = "Name";
        int age = 22;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.getStudentByAge(any(Integer.class))).thenReturn(List.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?age=" + age)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(student.getName()).isEqualTo(name);
                    assertThat(student.getAge()).isEqualTo(age);
                    assertThat(student.getId()).isEqualTo(id);
                });
    }
}
