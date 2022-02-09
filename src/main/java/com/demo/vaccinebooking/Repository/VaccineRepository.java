package com.demo.vaccinebooking.Repository;

import com.demo.vaccinebooking.Model.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineRepository extends JpaRepository<Vaccine, String> {
    Vaccine getByVaccineName(String name);
}
