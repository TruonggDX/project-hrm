package com.exo.hrm_project.exception;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(err -> err.getDefaultMessage())
        .toList();

    Map<String, Object> body = new HashMap<>();
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("errors", errors);

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleNotFound(NotFoundException ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("timestamp", ZonedDateTime.now());
    error.put("status", HttpStatus.NOT_FOUND.value());
    error.put("message", ex.getMessage());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }


}
