package com.openclassrooms.safetynet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {PersonNotFoundException.class})
    public ResponseEntity<Object> handlePersonNotFoundException(PersonNotFoundException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {MailsNotFoundException.class})
    public ResponseEntity<Object> handleMailsNotFoundException(MailsNotFoundException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {FireStationNotFoundException.class})
    public ResponseEntity<Object> handleFireStationNotFoundException(FireStationNotFoundException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {MedicalRecordNotFoundException.class})
    public ResponseEntity<Object> handleMedicalNotFoundException(MedicalRecordNotFoundException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {PersonAlreadyExistException.class})
    public ResponseEntity<Object> handlePersonAlreadyExistException(PersonAlreadyExistException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MedicalRecordAlreadyExistException.class})
    public ResponseEntity<Object> handleMedicalRecordAlreadyExistException(MedicalRecordAlreadyExistException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {FireStationAlreadyExistException.class})
    public ResponseEntity<Object> handleFireStationAlreadyExistException(FireStationAlreadyExistException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }


}
