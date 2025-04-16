package com.boardcamp.api.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long gameId;

    @Column(nullable = false)
    private LocalDate rentDate;

    @Column(nullable = false)
    private Integer daysRented;

    private LocalDate returnDate;

    @Column(nullable = false)
    private Integer originalPrice;

    private Integer delayFee;
}
