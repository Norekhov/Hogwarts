package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}")
    public Faculty get(@PathVariable long id) {
        return facultyService.get(id);
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id,
                       @RequestBody Faculty faculty) {
        facultyService.update(id,faculty);
    }

    @DeleteMapping("{id}")
    public Faculty remove(@PathVariable long id) {
        return facultyService.remove(id);
    }

    @GetMapping
    public List<Faculty> getFacultyByColor(@RequestParam String color) {
        return facultyService.getFacultyByColor(color);
    }
}


