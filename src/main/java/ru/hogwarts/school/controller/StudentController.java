package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
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
        studentService.update(id,student);
    }

    @DeleteMapping("{id}")
    public Student remove(@PathVariable long id) {
        return studentService.remove(id);
    }

    @GetMapping
    public List<Student> getStudentByAge(@RequestParam int age) {
        return studentService.getStudentByAge(age);
    }
}
