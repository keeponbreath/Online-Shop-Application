package spring.cloud.itemservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.cloud.itemservice.exception.ExceptionResponse;
import spring.cloud.itemservice.exception.ItemException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ItemsControllerAdvice {
    @ExceptionHandler({IllegalAccessException.class, ItemException.class})
    public ResponseEntity<ExceptionResponse> handle(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handle(NoSuchElementException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handle(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }
}
