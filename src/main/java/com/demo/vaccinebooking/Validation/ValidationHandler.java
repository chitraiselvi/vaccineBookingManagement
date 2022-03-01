package com.demo.vaccinebooking.Validation;

import com.demo.vaccinebooking.Exception.VaccineBookingException;
import com.demo.vaccinebooking.Model.APIResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice ;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationHandler extends ResponseEntityExceptionHandler{
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        APIResponse apiResponse = new APIResponse();
        List<Object> objectList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            objectList.add(error.getDefaultMessage());
        });
        apiResponse.setSuccess(false);
        apiResponse.setMessage("There is a technical error.");
        apiResponse.setData(objectList);
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({VaccineBookingException.class})
    public ResponseEntity<Object> handleVaccineBookingException(VaccineBookingException ex, WebRequest request){
        return createResponse(ex, ex.getStatus());
    }

    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex){
        return createResponse(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        return createResponse(ex, HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
        return createResponse(ex, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        APIResponse apiResponse = new APIResponse();
        List<Object> objectList = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            objectList.add(violation.getMessage());
        }
        apiResponse.setSuccess(false);
        apiResponse.setMessage("There is a technical error.");
        apiResponse.setData(objectList);
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<Object> createResponse(Exception ex, HttpStatus status){
        APIResponse apiResponse = new APIResponse();
        List<Object> objectList = new ArrayList<>();
        objectList.add(ex.getMessage());
        apiResponse.setSuccess(false);
        apiResponse.setMessage("There is a technical error.");
        apiResponse.setData(objectList);
        return new ResponseEntity<>(apiResponse, status);
    }

}
