package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long lastId = 1;

    public Student create (Student student) {
        student.setId(lastId++);
        students.put(student.getId(), student);
        return student;
    }

    public void update(long id, Student student) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
        student.setId(id);
        students.put(id, student);
    }

    public Student get(long id) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
        return students.get(id);
    }

    public Student remove (long id) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
        return students.remove(id);
    }

    public List<Student> getStudentByAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge()==age)
                .toList();
    }
}