package com.demo.vaccinebooking.Controller;

import com.demo.vaccinebooking.Exception.VaccineBookingException;
import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Booking;
import com.demo.vaccinebooking.Service.VaccineBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@RestController
@Validated
public class VaccineBookingController {

    @Autowired
    VaccineBookingService vaccineBookingService;

    @GetMapping("/availableSlots")
    public ResponseEntity<APIResponse> getAvailableSlots(
            @Valid @NotBlank(message = "Vaccine Name cannot be blank") @RequestParam String vaccineName,
            @Valid @NotBlank(message = "Date cannot be blank") @Pattern(regexp = "[0-9][0-9][0-9][0-9]-[0-1][1-9]-[0-3][0-9]", message = "Invalid Date. The format should be YYYY-MM-DD") @RequestParam String date
    ) throws Exception{
        return vaccineBookingService.getAvailableSlots(vaccineName,date);
    }

    @PostMapping("/booking")
    public ResponseEntity<APIResponse> createBooking(@Valid @RequestBody Booking booking) throws Exception {
        return vaccineBookingService.createBooking(booking);
    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<APIResponse> getBooking(@Valid @NotNull(message = "Booking Id cannot be blank") @PathVariable int id) throws Exception{
        return vaccineBookingService.getBookingById(id);
    }

    @PutMapping("/updateBooking")
    public ResponseEntity<APIResponse> updateBooking(@Valid @RequestBody Booking booking) throws Exception {
        return vaccineBookingService.updateBooking(booking);
    }

    @DeleteMapping("/cancelBooking/{bookingId}")
    public ResponseEntity<APIResponse> cancelBooking(@Valid @NotNull(message = "Booking Id cannot be blank")  @PathVariable int bookingId) throws Exception {
        return vaccineBookingService.deleteBooking(bookingId);
    }

    @DeleteMapping("/deleteAllBookings")
    public ResponseEntity<APIResponse> deleteAllBookings(){
        return vaccineBookingService.deleteAllBookings();
    }
}
