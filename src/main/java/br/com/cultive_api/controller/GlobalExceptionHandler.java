package br.com.cultive_api.controller;

import br.com.cultive_api.exception.CannotDeleteAdminException;
import br.com.cultive_api.exception.CannotRemoveAdminRoleException;
import br.com.cultive_api.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "User not found",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(CannotRemoveAdminRoleException.class)
    public ResponseEntity<Object> handleCannotRemoveAdminRole(CannotRemoveAdminRoleException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.FORBIDDEN.value(),
                "error", "Forbidden action",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(CannotDeleteAdminException.class)
    public ResponseEntity<Object> handleCannotDeleteAdmin(CannotDeleteAdminException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.FORBIDDEN.value(),
                "error", "Forbidden action",
                "message", ex.getMessage()
        ));
    }

}
