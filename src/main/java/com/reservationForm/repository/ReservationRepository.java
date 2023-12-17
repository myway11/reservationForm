package com.reservationForm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservationForm.entity.ReservationEntity;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

}
