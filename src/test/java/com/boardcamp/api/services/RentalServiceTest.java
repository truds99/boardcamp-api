package com.boardcamp.api.services;

import com.boardcamp.api.exceptions.NoStockAvailableException;
import com.boardcamp.api.models.Customer;
import com.boardcamp.api.models.Game;
import com.boardcamp.api.models.Rental;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.repositories.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RentalServiceTest {

    private RentalService rentalService;
    private RentalRepository rentalRepository;
    private CustomerRepository customerRepository;
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        rentalRepository = mock(RentalRepository.class);
        customerRepository = mock(CustomerRepository.class);
        gameRepository = mock(GameRepository.class);
        rentalService = new RentalService(rentalRepository, customerRepository, gameRepository);
    }

    @Test
    void shouldThrowIfNoStockAvailable() {
        Game game = new Game();
        game.setId(1L);
        game.setStockTotal(1);
        game.setPricePerDay(1000);

        Rental existingRental = new Rental();
        existingRental.setGameId(1L);
        existingRental.setReturnDate(null);

        Customer customer = new Customer();
        customer.setId(1L);

        Rental rental = new Rental();
        rental.setCustomerId(1L);
        rental.setGameId(1L);
        rental.setDaysRented(5);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rentalRepository.findByGameIdAndReturnDateIsNull(1L)).thenReturn(List.of(existingRental));

        assertThrows(NoStockAvailableException.class, () -> rentalService.createRental(rental));
    }

    @Test
    void shouldCreateRentalSuccessfully() {
        Game game = new Game();
        game.setId(1L);
        game.setStockTotal(2);
        game.setPricePerDay(1000);

        Customer customer = new Customer();
        customer.setId(1L);

        Rental rental = new Rental();
        rental.setCustomerId(1L);
        rental.setGameId(1L);
        rental.setDaysRented(5);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rentalRepository.findByGameIdAndReturnDateIsNull(1L)).thenReturn(List.of());
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Rental created = rentalService.createRental(rental);

        assertNotNull(created);
        assertEquals(1L, created.getCustomerId());
        assertEquals(1L, created.getGameId());
        assertEquals(5, created.getDaysRented());
        assertEquals(5000, created.getOriginalPrice()); 
    }

    @Test
    void shouldFinishRentalWithDelayFee() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setRentDate(LocalDate.now().minusDays(7));
        rental.setDaysRented(3); 
        rental.setOriginalPrice(3000); 
        rental.setReturnDate(null);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Rental finished = rentalService.finishRental(1L);

        assertNotNull(finished.getReturnDate());
        assertEquals(4000, finished.getDelayFee()); 
    }

    @Test
    void shouldThrowWhenRentalAlreadyFinished() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setReturnDate(LocalDate.now()); 

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        assertThrows(IllegalStateException.class, () -> rentalService.finishRental(1L));
    }

    @Test
    void shouldThrowWhenDeletingUnfinishedRental() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setReturnDate(null);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        assertThrows(IllegalStateException.class, () -> rentalService.deleteRental(1L));
    }

    @Test
    void shouldDeleteRentalSuccessfully() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setReturnDate(LocalDate.now()); // já finalizado

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        rentalService.deleteRental(1L);

        verify(rentalRepository, times(1)).delete(rental);
    }


}
