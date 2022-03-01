package com.demo.vaccinebooking.Service;

import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Slot;
import com.demo.vaccinebooking.Repository.SlotRepository;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SlotService {
    Logger logger = LoggerFactory.logger(SlotService.class);

    @Autowired
    SlotRepository slotRepository;

   public ResponseEntity<APIResponse> addSlots(List<Slot> slots)
   {
       logger.info("new slots added");
       APIResponse apiResponse = new APIResponse();
       apiResponse.setSuccess(true);
       List<Object> objectList = new ArrayList<>();
       slotRepository.saveAll(slots).stream().forEach(slot -> objectList.add(slot));
       apiResponse.setData(objectList);
       return new ResponseEntity<>(apiResponse, HttpStatus.OK);
   }

    public Slot saveSlot(Slot slot){
        return slotRepository.save(slot);
    }
    //to get slots by date
    public List<Slot> getSlotsByDate(LocalDate date)
    {
        return slotRepository.findAllByAvailableDate(date);
    }

    //to get slots by id
    public Optional<Slot> getSlotsById(int id)
    {
        return slotRepository.findById(id);
    }

    //assign slot to the booking
    public void assignSlotToBooking(int slotId){
        //assign slot to the booking
        logger.info("assign slot to the booking");
        Slot bookedSlot = getSlotsById(slotId).get();
        bookedSlot.setAvailable(false);
        saveSlot(bookedSlot);
    }

    //unassign slot from the booking
    public void unassignSlotFromBooking(int slotId){
        //unassign slot from the booking
        logger.info("unassign slot from the booking");
        Slot bookedSlot = getSlotsById(slotId).get();
        bookedSlot.setAvailable(true);
        saveSlot(bookedSlot);
    }
}
