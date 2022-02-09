package com.demo.vaccinebooking.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
//@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int slotId;
    private LocalDate availableDate;
    private LocalTime availableTime;
    private boolean isAvailable;

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int id) {
        this.slotId = id;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public LocalTime getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(LocalTime availableTime) {
        this.availableTime = availableTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Slot(){

    }
    public Slot( LocalDate availableDate, LocalTime availableTime, boolean isAvailable) {
       // this.slotId = slotId;
        this.availableDate = availableDate;
        this.availableTime = availableTime;
        this.isAvailable = isAvailable;
    }
}
