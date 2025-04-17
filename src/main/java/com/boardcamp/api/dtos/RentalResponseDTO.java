package com.boardcamp.api.dtos;

import com.boardcamp.api.models.Customer;
import com.boardcamp.api.models.Game;
import com.boardcamp.api.models.Rental;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RentalResponseDTO {
    private Long id;
    private LocalDate rentDate;
    private Integer daysRented;
    private LocalDate returnDate;
    private Integer originalPrice;
    private Integer delayFee;
    private Customer customer;
    private Game game;

    public RentalResponseDTO(Rental rental, Customer customer, Game game) {
        this.id = rental.getId();
        this.rentDate = rental.getRentDate();
        this.daysRented = rental.getDaysRented();
        this.returnDate = rental.getReturnDate();
        this.originalPrice = rental.getOriginalPrice();
        this.delayFee = rental.getDelayFee();
        this.customer = customer;
        this.game = game;
    }
}
