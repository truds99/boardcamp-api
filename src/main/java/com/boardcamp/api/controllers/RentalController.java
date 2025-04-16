package com.boardcamp.api.controllers;

import com.boardcamp.api.exceptions.NoStockAvailableException;
import com.boardcamp.api.models.Rental;
import com.boardcamp.api.services.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.listRentals());
    }

    @PostMapping
    public ResponseEntity<Rental> createRental(@RequestBody Rental rental) {
        try {
            Rental created = rentalService.createRental(rental);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (NoStockAvailableException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
        
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Rental> returnRental(@PathVariable Long id) {
        try {
            Rental finished = rentalService.finishRental(id);
            return ResponseEntity.ok(finished);
        } catch (IllegalStateException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        try {
            rentalService.deleteRental(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        } 
    }


}
