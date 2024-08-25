package ru.hogwarts.school.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
@Tag(name = "Страница работы со студентами")
public class StudentController {

    private final StudentService studentService;
    private final AvatarService avatarService;

    public StudentController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Поиск студента")
    public Student get(@PathVariable long id) {
        return studentService.get(id);
    }

    @PostMapping
    @Operation(summary = "Создание студента")
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение студента")
    public void update(@PathVariable long id,
                       @RequestBody Student student) {
        studentService.update(id, student);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удаление студента")
    public Student remove(@PathVariable long id) {
        return studentService.remove(id);
    }

    @GetMapping(params = "age")
    @Operation(summary = "Поиск студентов по возрасту")
    public List<Student> getStudentByAge(@RequestParam int age) {
        return studentService.getStudentByAge(age);
    }

    @GetMapping
    @Operation(summary = "Поиск студентов по минимальному и максимальному возрасту")
    public List<Student> findByAgeBetween(@RequestParam("minAge") int minAge,
                                          @RequestParam("maxAge") int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    @Operation(summary = "Поиск факультета по ID студента")
    public Faculty findStudentsByFaculty(@PathVariable long id) {
        return studentService.findStudentsByFaculty(id);
    }

    @GetMapping("/{id}/avatar-from-db")
    @Operation(summary = "Добавление аватара в базу данных")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable long id) {
        return buildResponseEntity(avatarService.getAvatarFromDb(id));
    }

    @GetMapping("/{id}/avatar-from-fs")
    @Operation(summary = "Добавление аватара меньшего размера")
    public ResponseEntity<byte[]> getAvatarFromFs(@PathVariable long id) {
        return buildResponseEntity(avatarService.getAvatarFromFs(id));
    }

    public ResponseEntity<byte[]> buildResponseEntity(Pair<byte[], String> pair) {
        byte[] date = pair.getFirst();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(date.length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(date);
    }

    @GetMapping("/count")
    @Operation(summary = "Получение количества всех студентов")
    public long getCountStudents() {
        return studentService.getCountStudents();
    }

    @GetMapping("/age-avg")
    @Operation(summary = "Получение среднего возраста студентов")
    public double getAvgAgeStudents() {
        return studentService.getAvgAgeStudents();
    }

    @GetMapping("/desc-five")
    @Operation(summary = "Получение пяти последних студентов")
    public List<Student> getDescFiveStudents() {
        return studentService.getDescFiveStudents();
    }

    @GetMapping("/studentsWithNameStartingWithA")
    @Operation(summary = "Получение списка студентов, чье имя начинается на букву А")
    public List<String> getStudentsWithNameStartingWithA() {
        return studentService.getStudentsWithNameStartingWithA();
    }

    @GetMapping("/getTheAverageAgeOfStudents")
    @Operation(summary = "Получение среднего возраста всех студентов")
    public double getTheAverageAgeOfStudents() {
        return studentService.getTheAverageAgeOfStudents();
    }

    @GetMapping("/students/print-parallel")
    @Operation(summary = "Получение списка студентов в параллельном режиме")
    public void getAllStudentsParallelMode () {
         studentService.getAllStudentsParallelMode();
    }

    @GetMapping("/students/print-synchronized")
    @Operation(summary = "Получение списка студентов в синхронном режиме")
    public void getAllStudentsSynchronousMode () {
        studentService.getAllStudentsSynchronousMode();
    }
}
