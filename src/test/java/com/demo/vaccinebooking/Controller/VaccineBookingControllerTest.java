package com.demo.vaccinebooking.Controller;

import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Booking;
import com.demo.vaccinebooking.Service.VaccineBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;


@SpringBootTest
public class VaccineBookingControllerTest {
    @InjectMocks
    VaccineBookingController vaccineBookingController;
    @Mock
    VaccineBookingService vaccineBookingService;
    private ResponseEntity<APIResponse> response;
    private Booking booking;

    @BeforeEach
    public void init() {
        booking = new Booking();
        booking.setBookingId(1);
        booking.setEmailId("chitra@gmail.com");
        booking.setFirstName("chitra");
        booking.setLastName("manoj");
        booking.setVaccineName("Pfizer");
        booking.setMobileNo("1234567890");
       // booking.setSlot(slot1);
        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(true);
        response = new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Test
    public void getAvailableSlotsTest() throws Exception
    {
        when(vaccineBookingService.getAvailableSlots("Pfizer", "2022, 02, 19")).thenReturn(response);
        ResponseEntity<APIResponse> response = vaccineBookingController.getAvailableSlots("Pfizer", "2022, 02, 19");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void createBookingTest() throws Exception
    {
        when(vaccineBookingService.createBooking(any(Booking.class))).thenReturn(response);
        ResponseEntity<APIResponse> response = vaccineBookingController.createBooking(booking);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getBookingTest() throws Exception
    {
        when(vaccineBookingService.getBookingById(anyInt())).thenReturn(response);
        ResponseEntity<APIResponse> response = vaccineBookingController.getBooking(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void updateBookingTest() throws Exception
    {
        when(vaccineBookingService.updateBooking(any(Booking.class))).thenReturn(response);
        ResponseEntity<APIResponse> response = vaccineBookingController.updateBooking(booking);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void cancelBookingTest() throws Exception
    {
        when(vaccineBookingService.deleteBooking(anyInt())).thenReturn(response);
        ResponseEntity<APIResponse> response = vaccineBookingController.cancelBooking(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void deleteAllBookingTest() throws Exception
    {
        when(vaccineBookingService.deleteAllBookings()).thenReturn(response);
        ResponseEntity<APIResponse> response = vaccineBookingController.deleteAllBookings();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
