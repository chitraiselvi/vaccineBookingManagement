package com.demo.vaccinebooking.Service;

import com.demo.vaccinebooking.Model.Slot;
import com.demo.vaccinebooking.Repository.SlotRepository;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@Service
public class SlotService {
    Logger logger = LoggerFactory.logger(SlotService.class);

    @Autowired
    SlotRepository slotRepository;

   public List<Slot> addSlots(List<Slot> slots)
   {
       logger.info("new slots added");

       return slotRepository.saveAll(slots);
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
    public Slot getSlotsById(int id)
    {
        return slotRepository.getById(id);
    }

    //assign slot to the booking
    public void assignSlotToBooking(int slotId){
        //assign slot to the booking
        logger.info("assign slot to the booking");
        Slot bookedSlot = getSlotsById(slotId);
        bookedSlot.setIsAvailable(false);
        saveSlot(bookedSlot);
    }

    //unassign slot from the booking
    public void unassignSlotFromBooking(int slotId){
        //unassign slot from the booking
        logger.info("unassign slot from the booking");
        Slot bookedSlot = getSlotsById(slotId);
        bookedSlot.setIsAvailable(true);
        saveSlot(bookedSlot);
    }
}
