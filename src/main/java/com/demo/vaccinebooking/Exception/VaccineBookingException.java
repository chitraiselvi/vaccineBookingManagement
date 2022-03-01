package com.demo.vaccinebooking.Exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class VaccineBookingException extends Exception{
    @Getter
    @Setter
    private HttpStatus status;

    public VaccineBookingException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
}
