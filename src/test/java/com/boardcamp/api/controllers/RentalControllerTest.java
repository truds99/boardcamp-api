package com.boardcamp.api.controllers;

import com.boardcamp.api.models.Rental;
import com.boardcamp.api.services.RentalService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RentalControllerTest {

    private RentalService rentalService;
    private RentalController rentalController;

    @BeforeEach
    void setUp() {
        rentalService = mock(RentalService.class);
        rentalController = new RentalController(rentalService);
    }

    @Test
    void shouldReturn201WhenRentalIsCreated() {
        Rental rental = new Rental();
        rental.setCustomerId(1L);
        rental.setGameId(1L);
        rental.setDaysRented(5);
        rental.setRentDate(LocalDate.now());

        when(rentalService.createRental(any(Rental.class))).thenReturn(rental);

        ResponseEntity<Rental> response = rentalController.createRental(rental);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(rental, response.getBody());
    }

    @Test
    void shouldReturn400IfRentalDataIsInvalid() {
        Rental invalidRental = new Rental(); 

        when(rentalService.createRental(any(Rental.class)))
            .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<Rental> response = rentalController.createRental(invalidRental);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturn404IfGameOrCustomerNotFound() {
        Rental rental = new Rental();
        rental.setCustomerId(999L);
        rental.setGameId(999L);
        rental.setDaysRented(5);

        when(rentalService.createRental(any(Rental.class)))
            .thenThrow(new RuntimeException("Game or customer not found"));

        ResponseEntity<Rental> response = rentalController.createRental(rental);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturn422IfNoStockAvailable() {
        Rental rental = new Rental();
        rental.setCustomerId(1L);
        rental.setGameId(1L);
        rental.setDaysRented(5);

        when(rentalService.createRental(any(Rental.class)))
            .thenThrow(new com.boardcamp.api.exceptions.NoStockAvailableException("No stock available"));

        ResponseEntity<Rental> response = rentalController.createRental(rental);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void shouldReturn200WhenRentalIsFinished() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setReturnDate(LocalDate.now());

        when(rentalService.finishRental(1L)).thenReturn(rental);

        ResponseEntity<Rental> response = rentalController.finishRental(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rental, response.getBody());
    }


}
