package com.boardcamp.api.repositories;

import com.boardcamp.api.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByGameIdAndReturnDateIsNull(Long gameId);
}
