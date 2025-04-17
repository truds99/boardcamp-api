package com.boardcamp.api.services;

import com.boardcamp.api.exceptions.NoStockAvailableException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        existingRental.setGameId(game.getId());
        existingRental.setReturnDate(null);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(rentalRepository.findByGameIdAndReturnDateIsNull(1L)).thenReturn(List.of(existingRental));

        Rental rental = new Rental();
        rental.setGameId(1L);
        rental.setDaysRented(5);
        rental.setRentDate(LocalDate.now());
        rental.setCustomerId(1L); // Necessário para passar pela validação
        when(customerRepository.findById(1L)).thenReturn(Optional.of(mock()));

        assertThrows(NoStockAvailableException.class, () -> rentalService.createRental(rental));
    }
}
