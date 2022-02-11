package com.demo.vaccinebooking.Controller;

import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Booking;
import com.demo.vaccinebooking.Service.VaccineBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class VaccineBookingController {

    @Autowired
    VaccineBookingService vaccineBookingService;

    @GetMapping("/availableSlots")
    public ResponseEntity<APIResponse> getAvailableSlots(@RequestParam String vaccineName, @RequestParam String date){
        return vaccineBookingService.getAvailableSlots(vaccineName,date);
    }

    @PostMapping("/booking")
    public ResponseEntity<APIResponse> createBooking(@RequestBody Booking booking){
        return vaccineBookingService.createBooking(booking);
    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<APIResponse> getBooking(@PathVariable int id){
        return vaccineBookingService.getBookingById(id);
    }

    @PutMapping("/updateBooking")
    public ResponseEntity<APIResponse> updateBooking(@RequestBody Booking booking){
        return vaccineBookingService.updateBooking(booking);
    }

    @DeleteMapping("/cancelBooking/{bookingId}")
    public ResponseEntity<APIResponse> cancelBooking(@PathVariable int bookingId){
        return vaccineBookingService.deleteBooking(bookingId);
    }

    @DeleteMapping("/deleteAllBookings")
    public ResponseEntity<APIResponse> deleteAllBookings(){
        return vaccineBookingService.deleteAllBookings();
    }
}
