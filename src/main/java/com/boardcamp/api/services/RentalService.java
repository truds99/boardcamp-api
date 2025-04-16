package com.boardcamp.api.services;

import com.boardcamp.api.models.Customer;
import com.boardcamp.api.models.Game;
import com.boardcamp.api.models.Rental;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.repositories.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final GameRepository gameRepository;

    public RentalService(
        RentalRepository rentalRepository,
        CustomerRepository customerRepository,
        GameRepository gameRepository
    ) {
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
        this.gameRepository = gameRepository;
    }

    public List<Rental> listRentals() {
        return rentalRepository.findAll();
    }

    public Rental createRental(Rental rental) {
        if (rental.getDaysRented() == null || rental.getDaysRented() <= 0) {
            throw new IllegalArgumentException("Invalid rental duration.");
        }

        if (rental.getCustomerId() == null || rental.getGameId() == null) {
            throw new IllegalArgumentException("Customer and game must be provided.");
        }

        Customer customer = customerRepository.findById(rental.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Game game = gameRepository.findById(rental.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));

        int openRentals = rentalRepository
                .findByGameIdAndReturnDateIsNull(rental.getGameId()).size();

        if (openRentals >= game.getStockTotal()) {
            throw new RuntimeException("No stock available.");
        }

        Rental newRental = new Rental();
        newRental.setCustomerId(customer.getId());
        newRental.setGameId(game.getId());
        newRental.setRentDate(LocalDate.now());
        newRental.setDaysRented(rental.getDaysRented());
        newRental.setReturnDate(null);
        newRental.setOriginalPrice(game.getPricePerDay() * rental.getDaysRented());
        newRental.setDelayFee(0);

        return rentalRepository.save(newRental);
    }

    public Rental finishRental(Long id) {
        Rental rental = rentalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rental not found"));
    
        if (rental.getReturnDate() != null) {
            throw new IllegalStateException("Rental already finished");
        }
    
        LocalDate today = LocalDate.now();
        LocalDate expectedReturnDate = rental.getRentDate().plusDays(rental.getDaysRented());
    
        int delayDays = today.isAfter(expectedReturnDate)
            ? (int) expectedReturnDate.until(today).getDays()
            : 0;
    
        int delayFee = delayDays * (rental.getOriginalPrice() / rental.getDaysRented());
    
        rental.setReturnDate(today);
        rental.setDelayFee(delayFee);
    
        return rentalRepository.save(rental);
    }

    public void deleteRental(Long id) {
        Rental rental = rentalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rental not found"));
    
        if (rental.getReturnDate() == null) {
            throw new IllegalStateException("Cannot delete rental not yet finished");
        }
    
        rentalRepository.delete(rental);
    }
    
    
}

