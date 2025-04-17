package com.boardcamp.api.integration;

import com.boardcamp.api.models.Customer;
import com.boardcamp.api.models.Game;
import com.boardcamp.api.models.Rental;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.repositories.RentalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RentalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RentalRepository rentalRepository;

    private Customer customer;
    private Game game;

    @BeforeEach
    void setUp() {
        rentalRepository.deleteAll();
        customerRepository.deleteAll();
        gameRepository.deleteAll();

        customer = new Customer();
        customer.setName("João Teste");
        customer.setCpf("01234567890");
        customer.setPhone("21999999999");
        customer = customerRepository.save(customer);

        game = new Game();
        game.setName("Detetive");
        game.setImage("http://");
        game.setStockTotal(5);
        game.setPricePerDay(1500);
        game = gameRepository.save(game);
    }

    @Test
    void shouldCreateRentalSuccessfully() throws Exception {
        Rental rental = new Rental();
        rental.setCustomerId(customer.getId());
        rental.setGameId(game.getId());
        rental.setDaysRented(3);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/rentals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rental)))
                .andExpect(status().isCreated());
    }
}