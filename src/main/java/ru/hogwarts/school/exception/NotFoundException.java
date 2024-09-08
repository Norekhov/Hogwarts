package ru.hogwarts.school.exception;
/**
*Creating a class to throw an exception
*/
public abstract class NotFoundException extends RuntimeException{

    private long id;

    public long getId() {
        return id;
    }

    public NotFoundException(long id) {
        this.id = id;
    }
}
