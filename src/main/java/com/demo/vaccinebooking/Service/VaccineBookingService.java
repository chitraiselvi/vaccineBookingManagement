package com.demo.vaccinebooking.Service;

import com.demo.vaccinebooking.Exception.VaccineBookingException;
import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Booking;
import com.demo.vaccinebooking.Model.Slot;
import com.demo.vaccinebooking.Model.Vaccine;
import com.demo.vaccinebooking.Repository.BookingRepository;
import com.demo.vaccinebooking.Repository.SlotRepository;
import com.demo.vaccinebooking.Repository.VaccineRepository;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.web.bind.MethodArgumentNotValidException;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class VaccineBookingService {
    Logger logger = LoggerFactory.logger(VaccineBookingService.class);

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    VaccineService vaccineService;
    @Autowired
    SlotService slotService;

    //to get available slots for the given vaccine and date
    public ResponseEntity<APIResponse> getAvailableSlots(String vaccineName, String date) throws Exception{
        logger.info("get all available slots for a vaccine and date");
        APIResponse apiResponse = new APIResponse();
        LocalDate selectedDate = LocalDate.parse(date);
        //get vaccine by name
        Optional<Vaccine> vaccine = vaccineService.getVaccineByName(vaccineName);
        //to check the vaccine availability
        if (!vaccine.isPresent() || vaccine.get().getAvailableShots() < 1) {
            throw new VaccineBookingException("The vaccine you have given is unavailable. Please select a different vaccine", HttpStatus.NOT_FOUND);
        } else {
            //get available slots
            List<Slot> slots = slotService.getSlotsByDate(selectedDate);
            if (slots == null || slots.size() < 1) {
                throw new VaccineBookingException("There are no slots available. Please try some other date.", HttpStatus.FORBIDDEN);
            } else {
                apiResponse.setSuccess(true);
                apiResponse.setMessage("Here are the available slots for you.");
                List<Object> objectList = new ArrayList<>();
                for (Slot slot : slots) {
                    if (slot.isAvailable()) {
                        objectList.add(slot);
                    }
                }
                //slots.stream().forEach(slot -> objectList.add(slot));
                apiResponse.setData(objectList);
                return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
            }
        }
    }

    //to create booking
    public ResponseEntity<APIResponse> createBooking(Booking booking) throws Exception {
        logger.info("create a booking");
        APIResponse apiResponse = new APIResponse();

        //get slot using slot id from the payload
        int assingedSlotId = booking.getSlot().getSlotId();
        Optional<Slot> assignedSlot = slotService.getSlotsById(assingedSlotId);
        //check if the slot is not present or if it is unavailable
        if(!assignedSlot.isPresent() || assignedSlot.get().isAvailable() == false){
            throw new VaccineBookingException("The slot you have selected is unavailable. Please choose a different time slot.", HttpStatus.NOT_FOUND);
        }

        //get vaccine using vaccine name
        Optional<Vaccine> vaccine = vaccineService.getVaccineByName(booking.getVaccineName());
        //check if the vaccine is not available
        if(!vaccine.isPresent() || vaccine.get().getAvailableShots() < 1){
            throw new VaccineBookingException("The vaccine you have selected is unavailable. Please choose a different vaccine.", HttpStatus.NOT_FOUND);
        }
        logger.info("success - we can create booking");
        //create booking
        Booking createdBooking = bookingRepository.save(booking);
        logger.info("assign slot");
        //assign slot to the booking
        slotService.assignSlotToBooking(assingedSlotId);
        //decrease available shots count
        vaccineService.decreaseVaccineShots(booking.getVaccineName());
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Your appointment has been booked successfully. Here is your booking id : "+createdBooking.getBookingId());
        return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.OK);

    }

    public Optional<Booking> getBooking(int bookingId){
        return bookingRepository.findById(bookingId);
    }

    //to get Booking
    public ResponseEntity<APIResponse> getBookingById(int bookingId) throws Exception {
        logger.info("Get Booking");
        APIResponse apiResponse = new APIResponse();
        Optional<Booking> booking = getBooking(bookingId);
        if(!booking.isPresent() || booking.get().getVaccineName() == null ){
            throw new VaccineBookingException("No booking details available for this booking id.", HttpStatus.NOT_FOUND);
        }
        else {
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Here is your booking detail");
            List<Object> objectList = new ArrayList<>();
            objectList.add(booking);
            apiResponse.setData(objectList);
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
        }
    }

    //to update booking
    public ResponseEntity<APIResponse> updateBooking(Booking booking) throws Exception {
        logger.info("update booking");
        APIResponse apiResponse = new APIResponse();
        int bookingId = booking.getBookingId();
        Optional<Booking> oldBooking = getBooking(bookingId);
        if(!oldBooking.isPresent()){
            throw new VaccineBookingException("No booking details available for this booking id.", HttpStatus.NOT_FOUND);
        }
        else {
            //check if vaccine is changed
            String vaccineName = booking.getVaccineName();
            String oldVaccineName = oldBooking.get().getVaccineName();
            if (!vaccineName.equals(oldVaccineName)) {
                throw new VaccineBookingException("Vaccine cannot be updated. Please cancel your booking and create a new one.", HttpStatus.FORBIDDEN);
            }

            int oldSlotId = oldBooking.get().getSlot().getSlotId();
            int newSlotId = booking.getSlot().getSlotId();
            //reassign slots if slotId changed
            if (oldSlotId != newSlotId) {
                //check if the slot is available
                Optional<Slot> assignedSlot = slotService.getSlotsById(newSlotId);
                if (!assignedSlot.isPresent() || assignedSlot.get().isAvailable() == false) {
                    throw new VaccineBookingException("The slot you have selected is unavailable. Please choose a different time slot.", HttpStatus.NOT_FOUND);
                }
                slotService.unassignSlotFromBooking(oldSlotId);
                slotService.assignSlotToBooking(booking.getSlot().getSlotId());
            }

            apiResponse.setSuccess(true);
            apiResponse.setMessage("Your booking has been updated successfully.");
            bookingRepository.save(booking);
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
        }
    }

    public ResponseEntity<APIResponse> deleteBooking(int bookingId) throws Exception {
        logger.info("delete booking");
        APIResponse apiResponse = new APIResponse();
        //unassign slot from booking
        Optional<Booking> oldBooking = getBooking(bookingId);
        if(!oldBooking.isPresent()){
            throw new VaccineBookingException("No booking details available for this booking id.", HttpStatus.NOT_FOUND);
        }
        else {
            //unassign slot
            int oldSlotId = oldBooking.get().getSlot().getSlotId();
            slotService.unassignSlotFromBooking(oldSlotId);
            //increase vaccine count
            vaccineService.increaseVaccineShots(oldBooking.get().getVaccineName());
            //delete booking
            logger.info("delete booking");
            bookingRepository.deleteById(bookingId);

            apiResponse.setSuccess(true);
            apiResponse.setMessage("Your booking has been cancelled successfully.");
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
        }
    }

    public ResponseEntity<APIResponse> deleteAllBookings(){
        logger.info("delete all bookings");
        APIResponse apiResponse = new APIResponse();
        bookingRepository.deleteAll();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("All bookings have been cancelled successfully.");
        return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
    }
}
