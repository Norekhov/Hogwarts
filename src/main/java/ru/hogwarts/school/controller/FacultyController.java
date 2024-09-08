package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
/**
*Creating a controller to implement enpoints facultyService
*/
@RestController
@RequestMapping("/faculty")
@Tag(name="Страница работы с факультетами")
public class FacultyController {

    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }
/**
*Implementations of the faculty search enpoint
*/
    @GetMapping("/{id}")
    @Operation(summary = "Поиск факультета")
    public Faculty get(@PathVariable long id) {
        return facultyService.get(id);
    }
/**
*Implementation of the faculty creation enpoint
*/
    @PostMapping
    @Operation(summary = "Создание факультета")
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }
/**
*Implementation of the faculty change enpoint
*/
    @PutMapping("/{id}")
    @Operation(summary = "Изменение факультета")
    public void update(@PathVariable long id,
                       @RequestBody Faculty faculty) {
        facultyService.update(id, faculty);
    }
/**
*Creating a controller to implement enpoints facultyService
*/
    @DeleteMapping("{id}")
    @Operation(summary = "Удаление факультета")
    public Faculty remove(@PathVariable long id) {
        return facultyService.remove(id);
    }
/**
*Implementation of the faculty removal enpoint
*/
    @GetMapping(params = "color")
    @Operation(summary = "Поиск факультета по цвету")
    public List<Faculty> getFacultyByColor(@RequestParam String color) {
        return facultyService.getFacultyByColor(color);
    }
/**
*Implementation of enpoint to search for a faculty by color or name
*/
    @GetMapping(params = "colorOrName")
    @Operation(summary = "Поиск факультета по цвету или имени")
    public List<Faculty> getFacultyByColorOrName(@RequestParam String colorOrName) {
        return facultyService.getFacultyByColorOrName(colorOrName);
    }
/**
*Implementation of an enpoint for searching students by faculty ID
*/
    @GetMapping("/{id}/students")
    @Operation(summary = "Поиск студентов по ID факультета")
    public List<Student> findStudentsByFacultyId(@PathVariable long id) {
        return facultyService.findStudentsByFacultyId(id);
    }
/**
*Implementation of the enpoint to obtain the longest faculty name
*/
    @GetMapping("/getTheLongestFacultyName")
    @Operation(summary = "Получение самого длинного названия факультета")
    public String getTheLongestFacultyName() {
        return facultyService.getTheLongestFacultyName();
    }
/**
*Implementations of enpoint to get integer value
*/
    @GetMapping("/getIntegerValue")
    @Operation(summary = "Получение целочисленного значения")
    public Integer getIntegerValue() {
        return facultyService.getIntegerValue();
    }
}


