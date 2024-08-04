package ru.hogwarts.school.controller;


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
public class StudentController {

    private final StudentService studentService;
    private final AvatarService avatarService;

    public StudentController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable long id) {
        return studentService.get(id);
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id,
                       @RequestBody Student student) {
        studentService.update(id, student);
    }

    @DeleteMapping("{id}")
    public Student remove(@PathVariable long id) {
        return studentService.remove(id);
    }

    @GetMapping(params = "age")
    public List<Student> getStudentByAge(@RequestParam int age) {
        return studentService.getStudentByAge(age);
    }

    @GetMapping
    public List<Student> findByAgeBetween(@RequestParam("minAge") int minAge,
                                          @RequestParam("maxAge")int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public Faculty findStudentsByFaculty(@PathVariable long id) {
        return studentService.findStudentsByFaculty(id);
    }

    @GetMapping("/{id}/avatar-from-db")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable long id) {
        return buildResponseEntity(avatarService.getAvatarFromDb(id));
    }

    @GetMapping("/{id}/avatar-from-fs")
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
}
