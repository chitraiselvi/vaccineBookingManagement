package com.demo.vaccinebooking.Repository;

import com.demo.vaccinebooking.Model.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VaccineRepository extends JpaRepository<Vaccine, String> {
    Optional<Vaccine> getByVaccineName(String name);
}
