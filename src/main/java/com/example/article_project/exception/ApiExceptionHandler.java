package com.example.article_project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest req, Exception ex) {
        return handleException(req, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest req, ArticleNotFoundException ex) {
        return handleException(req, ex, HttpStatus.BAD_REQUEST);
    }

    // Overloaded private method for handling exceptions
    private ResponseEntity<ErrorResponse> handleException(HttpServletRequest req, Exception ex, HttpStatus status) {
        System.out.println("url: " + req.getRequestURL() + ", method: " + req.getMethod() + ", ex : " + ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                                    .code(String.valueOf(status.value()))
                                    .message(ex.getMessage())
                                    .build();
        return ResponseEntity.ok().body(response);
    }
    
    // exceprtion handler method
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        
        log.info("uri: {}, method: {}", req.getRequestURI(), req.getMethod());

        StringBuilder builder = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            builder.append("[").append(fieldError.getField()).append("] ")
            .append(fieldError.getDefaultMessage())
            .append(", Provided value : ")
            .append(fieldError.getRejectedValue())
            .append("\n");
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                                    .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                                    .message(builder.toString())
                                    .build();

        return ResponseEntity.ok().body(errorResponse);
    }
}
