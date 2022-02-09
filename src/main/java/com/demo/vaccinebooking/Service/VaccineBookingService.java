package com.demo.vaccinebooking.Service;

import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Booking;
import com.demo.vaccinebooking.Model.Slot;
import com.demo.vaccinebooking.Model.Vaccine;
import com.demo.vaccinebooking.Repository.BookingRepository;
import com.demo.vaccinebooking.Repository.SlotRepository;
import com.demo.vaccinebooking.Repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public ResponseEntity<APIResponse> getAvailableSlots(String vaccineName, LocalDate date){
        logger.info("get all available slots for a vaccine and date");
        APIResponse apiResponse = new APIResponse();
        //get vaccine by name
        Vaccine vaccine = vaccineService.getVaccineByName(vaccineName);

        if(vaccine == null){
            apiResponse.setSuccess(false);
            apiResponse.setMessage("The vaccine you have given is unavailable. Please select a different vaccine");
        }
        else if(vaccine.getAvailableShots()>0){
            //get available slots
            List<Slot> slots = slotService.getSlotsByDate(date);
            if(slots == null || slots.size() < 1){
                apiResponse.setSuccess(false);
                apiResponse.setMessage("There are no slots available. Please try some other date.");
            }
            else{
                apiResponse.setSuccess(true);
                apiResponse.setMessage("Here are the available slots for you.");
                List<Object> objectList = new ArrayList<>();
                for(Slot slot:slots){
                    objectList.add(slot);
                }
                //slots.stream().forEach(slot -> objectList.add(slot));
                apiResponse.setData(objectList);
            }
        }else{
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Vaccine unavailable.");
        }
        return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
    }

    //to create booking
    public ResponseEntity<APIResponse> createBooking(Booking booking)
    {
        logger.info("create a booking");
        APIResponse apiResponse = new APIResponse();
        Boolean isSuccess = false;
        int assingedSlotId = booking.getSlot().getSlotId();
        try {
            Slot assignedSlot = slotService.getSlotsById(assingedSlotId);
            if(assignedSlot == null || assignedSlot.isAvailable() == false){
                apiResponse.setSuccess(false);
                apiResponse.setMessage("The slot you have selected is unavailable. Please choose a different time slot.");
                return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.FORBIDDEN);
            }
            else{
                isSuccess = true;
            }
        }
        catch (Exception e){
            logger.info(e);
            apiResponse.setSuccess(false);
            apiResponse.setMessage("The slot you have selected is unavailable. Please choose a different time slot.");
            return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.FORBIDDEN);
        }

        Vaccine vaccine = vaccineService.getVaccineByName(booking.getVaccineName());
        if(vaccine == null || vaccine.getAvailableShots() < 1){
            apiResponse.setSuccess(false);
            apiResponse.setMessage("The vaccine you have selected is unavailable. Please choose a different vaccine.");
            return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.FORBIDDEN);
        }
        else {
            isSuccess = true;
        }

        if(isSuccess){
            //assign slot to the booking
            slotService.assignSlotToBooking(assingedSlotId);
            //decrease available shots count
            vaccineService.decreaseVaccineShots(booking.getVaccineName());
            //create booking
            Booking createdBooking = bookingRepository.save(booking);
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Your appointment has been booked successfully. Here is your booking id : "+createdBooking.getBookingId());
            return  new ResponseEntity<APIResponse>(apiResponse,HttpStatus.OK);
        }
        return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.FORBIDDEN);
    }

    public Booking getBooking(int bookingId){
        return bookingRepository.getById(bookingId);
    }

    //to get Booking
    public ResponseEntity<APIResponse> getBookingById(int bookingId){
        logger.info("Get Booking");
        APIResponse apiResponse = new APIResponse();
        try{
            Booking booking = getBooking(bookingId);
            if(booking == null || booking.getVaccineName() == null ){
                apiResponse.setSuccess(false);
                apiResponse.setMessage("No booking details available for this booking id.");
                return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.NOT_FOUND);
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
        catch (Exception e) {
            logger.info(e);
            apiResponse.setSuccess(false);
            apiResponse.setMessage("No booking details available for this booking id.");
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    //to update booking
    public ResponseEntity<APIResponse> updateBooking(Booking booking)
    {
        logger.info("update booking");
        APIResponse apiResponse = new APIResponse();
        try {
            int bookingId = booking.getBookingId();
            Booking oldBooking = getBooking(bookingId);

            //check if vaccine is changed
            String vaccineName = booking.getVaccineName();
            String oldVaccineName = oldBooking.getVaccineName();
            if(!vaccineName.equalsIgnoreCase(oldVaccineName)){
                apiResponse.setSuccess(false);
                apiResponse.setMessage("Vaccine cannot be updated. Please cancel your booking and create a new one.");
                return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.FORBIDDEN);
            }

            int oldSlotId = oldBooking.getSlot().getSlotId();
            int newSlotId = booking.getSlot().getSlotId();
            //reassign slots if slotId changed
            if(oldSlotId != newSlotId) {
                //check if the slot is available
                Slot assignedSlot = slotService.getSlotsById(newSlotId);
                if(assignedSlot == null || assignedSlot.isAvailable() == false){
                    apiResponse.setSuccess(false);
                    apiResponse.setMessage("The slot you have selected is unavailable. Please choose a different time slot.");
                    return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.FORBIDDEN);
                }
                slotService.unassignSlotFromBooking(oldSlotId);
                slotService.assignSlotToBooking(booking.getSlot().getSlotId());
            }

            apiResponse.setSuccess(true);
            apiResponse.setMessage("Your booking has been updated successfully.");
            //List<Object> objectList = new ArrayList<>();
            //objectList.add(bookingRepository.save(booking));
            //apiResponse.setData(objectList);
            bookingRepository.save(booking);
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
        }
        catch (Exception e){
            logger.info(e);
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Your booking cannot be updated.");
            List<Object> objectList = new ArrayList<>();
            objectList.add(e);
            apiResponse.setData(objectList);
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<APIResponse> deleteBooking(int bookingId){
        logger.info("delete booking");
        APIResponse apiResponse = new APIResponse();
        try {
            //unassign slot from booking
            Booking oldBooking = getBooking(bookingId);
            int oldSlotId = oldBooking.getSlot().getSlotId();
            slotService.unassignSlotFromBooking(oldSlotId);
            //increase vaccine count
            vaccineService.increaseVaccineShots(oldBooking.getVaccineName());
            //delete booking
            logger.info("delete booking");
            bookingRepository.deleteById(bookingId);

            apiResponse.setSuccess(true);
            apiResponse.setMessage("Your booking has been cancelled successfully.");
            return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.OK);
        }
        catch (Exception e){
            logger.info(e);
            apiResponse.setSuccess(false);
            apiResponse.setMessage("There is a technical error while cancelling your booking.");
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<APIResponse> deleteAllBookings(){
        logger.info("delete all bookings");
        APIResponse apiResponse = new APIResponse();
        try {
            bookingRepository.deleteAll();
            //return "Bookings Deleted Successfully";
            apiResponse.setSuccess(true);
            apiResponse.setMessage("All bookings have been cancelled successfully.");
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
        }
        catch (Exception e){
            apiResponse.setSuccess(false);
            apiResponse.setMessage("There is a technical error while cancelling your booking.");
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.FORBIDDEN);
        }
    }
}
