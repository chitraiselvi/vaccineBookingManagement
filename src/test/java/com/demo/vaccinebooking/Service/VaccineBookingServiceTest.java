package com.demo.vaccinebooking.Service;

import com.demo.vaccinebooking.Exception.VaccineBookingException;
import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Booking;
import com.demo.vaccinebooking.Model.Slot;
import com.demo.vaccinebooking.Model.Vaccine;
import com.demo.vaccinebooking.Repository.BookingRepository;
import com.demo.vaccinebooking.Repository.SlotRepository;
import com.demo.vaccinebooking.Repository.VaccineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VaccineBookingServiceTest {
    @InjectMocks
    private VaccineBookingService bookingService;

    @Mock
    private VaccineService vaccineService;
    @Mock
    private SlotService slotService;
    @Mock
    private BookingRepository bookingRepository;



    private Vaccine vaccine;
    private List<Slot> slots;
    private Booking booking;

    @BeforeEach
    public void init() {
        slots =  new ArrayList<>();
        Slot slot1 = new Slot();
        slot1.setAvailableDate(LocalDate.of(2022,02,19));
        slot1.setAvailableTime(LocalTime.of(10,00));
        slot1.setAvailable(true);
        slot1.setSlotId(1);

        Slot slot2 = new Slot();
        slot2.setAvailableDate(LocalDate.of(2022,02,19));
        slot2.setAvailableTime(LocalTime.of(11,00));
        slot2.setAvailable(false);
        slot2.setSlotId(2);

        Slot slot3 = new Slot();
        slot3.setAvailableDate(LocalDate.of(2022,02,19));
        slot3.setAvailableTime(LocalTime.of(13,00));
        slot3.setAvailable(true);
        slot3.setSlotId(3);

        slots.add(slot1);
        slots.add(slot2);
        slots.add(slot3);

        vaccine = new Vaccine();
        vaccine.setVaccineId("Pf001");
        vaccine.setVaccineName("Pfizer");
        vaccine.setAvailableShots(100);

        booking = new Booking();
        booking.setBookingId(1);
        booking.setEmailId("chitra@gmail.com");
        booking.setFirstName("chitra");
        booking.setLastName("manoj");
        booking.setVaccineName("Pfizer");
        booking.setMobileNo("1234567890");
        booking.setSlot(slot1);
    }

    @Test
    public void getAvailableSlotsTest() throws Exception {
        when(vaccineService.getVaccineByName("Pfizer")).thenReturn(Optional.of(vaccine));
        when(slotService.getSlotsByDate(LocalDate.of(2022,02,19))).thenReturn(slots);
        ResponseEntity<APIResponse> response=bookingService.getAvailableSlots("Pfizer","2022-02-19");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getSuccess());
    }

    @Test
    public void getAvailableSlotsNegative1Test() throws Exception {
        when(vaccineService.getVaccineByName("Pfizer")).thenReturn(Optional.of(vaccine));
        when(slotService.getSlotsByDate(LocalDate.of(2022,02,19))).thenReturn(slots);
        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            bookingService.getAvailableSlots("Moderna","2022-02-19");
        });
        assertEquals("The vaccine you have given is unavailable. Please select a different vaccine", exception.getMessage());
    }

    @Test
    public void getAvailableSlotsNegative2Test() throws Exception {
        when(vaccineService.getVaccineByName("Pfizer")).thenReturn(Optional.of(vaccine));
        when(slotService.getSlotsByDate(LocalDate.of(2022,02,19))).thenReturn(slots);
        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            bookingService.getAvailableSlots("Pfizer","2022-02-27");
        });
        assertEquals("There are no slots available. Please try some other date.", exception.getMessage());
    }

    @Test
    public void createBookingTest() throws Exception {
        when(vaccineService.getVaccineByName("Pfizer")).thenReturn(Optional.of(vaccine));
        when(slotService.getSlotsById(1)).thenReturn(Optional.of(slots.get(0)));
        when(bookingRepository.save(booking)).thenReturn(booking);
        ResponseEntity<APIResponse> response= bookingService.createBooking(booking);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getSuccess());
    }

    @Test
    public void createBookingNegative1Test() throws Exception {
        when(vaccineService.getVaccineByName("Pfizer")).thenReturn(Optional.of(vaccine));
        when(slotService.getSlotsById(2)).thenReturn(Optional.of(slots.get(1)));
        when(bookingRepository.save(booking)).thenReturn(booking);
        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            bookingService.createBooking(booking);
        });
        assertEquals("The slot you have selected is unavailable. Please choose a different time slot.", exception.getMessage());
    }

    @Test
    public void createBookingNegative2Test() throws Exception {
        //when(vaccineService.getVaccineByName("Pfizer")).thenReturn(Optional.of(vaccine));
        when(slotService.getSlotsById(1)).thenReturn(Optional.of(slots.get(0)));
        when(bookingRepository.save(booking)).thenReturn(booking);
        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            bookingService.createBooking(booking);
        });
        assertEquals("The vaccine you have selected is unavailable. Please choose a different vaccine.", exception.getMessage());
    }

    @Test
    public void getBookingByIdTest() throws Exception
    {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        ResponseEntity<APIResponse> response=bookingService.getBookingById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getSuccess());
    }

    @Test
    public void getBookingByIdNegativeTest() throws Exception {
        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            bookingService.getBookingById(2);
        });
        assertEquals("No booking details available for this booking id.", exception.getMessage());
    }

    @Test
    public void updateBookingTest() throws Exception{
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(slotService.getSlotsById(3)).thenReturn(Optional.of(slots.get(2)));

        Booking bookingToUpdate = new Booking();
        bookingToUpdate.setBookingId(booking.getBookingId());
        bookingToUpdate.setEmailId(booking.getEmailId());
        bookingToUpdate.setFirstName(booking.getFirstName());
        bookingToUpdate.setLastName("Arichandran");
        bookingToUpdate.setVaccineName(booking.getVaccineName());
        bookingToUpdate.setMobileNo(booking.getMobileNo());
        bookingToUpdate.setSlot(slots.get(2));

        ResponseEntity<APIResponse> response= bookingService.updateBooking(bookingToUpdate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getSuccess());
    }

    @Test
    public void updateBookingNegative1Test() throws Exception{
        //when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(slotService.getSlotsById(3)).thenReturn(Optional.of(slots.get(2)));

        Booking bookingToUpdate = new Booking();
        bookingToUpdate.setBookingId(56);
        bookingToUpdate.setEmailId(booking.getEmailId());
        bookingToUpdate.setFirstName(booking.getFirstName());
        bookingToUpdate.setLastName("Arichandran");
        bookingToUpdate.setVaccineName(booking.getVaccineName());
        bookingToUpdate.setMobileNo(booking.getMobileNo());
        bookingToUpdate.setSlot(slots.get(2));

        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            bookingService.updateBooking(bookingToUpdate);
        });
        assertEquals("No booking details available for this booking id.", exception.getMessage());
    }

    @Test
    public void updateBookingNegative2Test() throws Exception{
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(slotService.getSlotsById(3)).thenReturn(Optional.of(slots.get(2)));

        Booking bookingToUpdate = new Booking();
        bookingToUpdate.setBookingId(booking.getBookingId());
        bookingToUpdate.setEmailId(booking.getEmailId());
        bookingToUpdate.setFirstName(booking.getFirstName());
        bookingToUpdate.setLastName("Arichandran");
        bookingToUpdate.setVaccineName("Moderna");
        bookingToUpdate.setMobileNo(booking.getMobileNo());
        bookingToUpdate.setSlot(slots.get(2));

        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            bookingService.updateBooking(bookingToUpdate);
        });
        assertEquals("Vaccine cannot be updated. Please cancel your booking and create a new one.", exception.getMessage());
    }

    @Test
    public void updateBookingNegative3Test() throws Exception{
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        //when(slotService.getSlotsById(3)).thenReturn(Optional.of(slots.get(2)));

        Booking bookingToUpdate = new Booking();
        bookingToUpdate.setBookingId(booking.getBookingId());
        bookingToUpdate.setEmailId(booking.getEmailId());
        bookingToUpdate.setFirstName(booking.getFirstName());
        bookingToUpdate.setLastName("Arichandran");
        bookingToUpdate.setVaccineName(booking.getVaccineName());
        bookingToUpdate.setMobileNo(booking.getMobileNo());
        bookingToUpdate.setSlot(slots.get(2));

        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            bookingService.updateBooking(bookingToUpdate);
        });
        assertEquals("The slot you have selected is unavailable. Please choose a different time slot.", exception.getMessage());
    }

    @Test
    public void deleteBookingTest() throws Exception
    {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        ResponseEntity<APIResponse> response=bookingService.deleteBooking(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getSuccess());

    }
    @Test
    public void deleteBookingNegativeTest() throws Exception {
      //  when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            bookingService.deleteBooking(6);
        });
        assertEquals("No booking details available for this booking id.", exception.getMessage());
    }

    @Test
    public void deleteAllBookingsTest() throws Exception
    {
        ResponseEntity<APIResponse> response=bookingService.deleteAllBookings();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getSuccess());

    }
}

