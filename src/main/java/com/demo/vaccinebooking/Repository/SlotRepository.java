package com.demo.vaccinebooking.Repository;

import com.demo.vaccinebooking.Model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot,Integer> {
    List<Slot> findAllByAvailableDate(LocalDate date);
}
