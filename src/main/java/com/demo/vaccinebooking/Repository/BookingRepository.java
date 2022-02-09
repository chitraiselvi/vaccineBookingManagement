package com.demo.vaccinebooking.Repository;

import com.demo.vaccinebooking.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Integer> {
}
