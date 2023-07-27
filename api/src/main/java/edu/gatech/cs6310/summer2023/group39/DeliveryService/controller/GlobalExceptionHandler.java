package edu.gatech.cs6310.summer2023.group39.DeliveryService.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DeliveryServiceException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeliveryServiceException.class)
    public ResponseEntity<String> handleYourException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
