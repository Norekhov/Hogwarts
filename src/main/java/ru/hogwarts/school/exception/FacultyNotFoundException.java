package ru.hogwarts.school.exception;
/**
*Creating a class to throw an exception to work with faculties
*/
public class FacultyNotFoundException extends NotFoundException{

    public FacultyNotFoundException(long id) {
        super(id);
    }

    @Override
    public String getMessage() {
        return "Факультет с id = %d, не найден".formatted(getId());
    }
}
