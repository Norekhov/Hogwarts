package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
@Tag(name="Страница работы с факультетами")
public class FacultyController {

    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Поиск факультета")
    public Faculty get(@PathVariable long id) {
        return facultyService.get(id);
    }

    @PostMapping
    @Operation(summary = "Создание факультета")
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение факультета")
    public void update(@PathVariable long id,
                       @RequestBody Faculty faculty) {
        facultyService.update(id, faculty);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удаление факультета")
    public Faculty remove(@PathVariable long id) {
        return facultyService.remove(id);
    }

    @GetMapping(params = "color")
    @Operation(summary = "Поиск факультета по цвету")
    public List<Faculty> getFacultyByColor(@RequestParam String color) {
        return facultyService.getFacultyByColor(color);
    }

    @GetMapping(params = "colorOrName")
    @Operation(summary = "Поиск факультета по цвету или имени")
    public List<Faculty> getFacultyByColorOrName(@RequestParam String colorOrName) {
        return facultyService.getFacultyByColorOrName(colorOrName);
    }

    @GetMapping("/{id}/students")
    @Operation(summary = "Поиск студентов по ID факультета")
    public List<Student> findStudentsByFacultyId(@PathVariable long id) {
        return facultyService.findStudentsByFacultyId(id);
    }
}


