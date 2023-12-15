package com.amazon.appproductservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<String> handle(RestException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
