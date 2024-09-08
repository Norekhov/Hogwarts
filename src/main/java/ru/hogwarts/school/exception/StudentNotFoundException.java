package ru.hogwarts.school.exception;
/**
*Creating a class to throw an exception to work with students
*/
public class StudentNotFoundException extends NotFoundException{
    public StudentNotFoundException(long id) {
        super(id);
    }

    @Override
    public String getMessage() {
        return "Студент с id = %d, не найден".formatted(getId());
    }
}
