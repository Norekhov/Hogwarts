package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/**
*Create a class to throw an exception when adding an avatar
*/
public class AvatarException extends RuntimeException{
    public ResponseEntity<String> handleAvatarException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Аватарка не читается");
    }
}
