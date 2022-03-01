package com.demo.vaccinebooking.Repository;

import com.demo.vaccinebooking.Model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot,Integer> {
    @Query(value = "SELECT * FROM SLOT WHERE IS_AVAILABLE = TRUE AND AVAILABLE_DATE=?1", nativeQuery = true)
    List<Slot> findAllByAvailableDate(LocalDate date);
}
