package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AvatarException extends RuntimeException{
    public ResponseEntity<String> handleAvatarException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Аватарка не читается");
    }
}
