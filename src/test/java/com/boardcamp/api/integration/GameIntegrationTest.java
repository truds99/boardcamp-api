package com.boardcamp.api.integration;

import com.boardcamp.api.models.Game;
import com.boardcamp.api.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanUpDatabase() {
        gameRepository.deleteAll();
    }

    @Test
    void shouldCreateGameSuccessfully() throws Exception {
        Game game = new Game();
        game.setName("Detetive");
        game.setImage("http://imagem.com/detetive.jpg");
        game.setStockTotal(5);
        game.setPricePerDay(2000);

        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Detetive"));
    }

    @Test
    void shouldReturn409WhenGameAlreadyExists() throws Exception {
        Game game = new Game();
        game.setName("Catan");
        game.setImage("http://imagem.com/catan.jpg");
        game.setStockTotal(3);
        game.setPricePerDay(2500);
        gameRepository.save(game);

        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldGetAllGames() throws Exception {
        Game game = new Game();
        game.setName("Ticket to Ride");
        game.setImage("http://imagem.com/ticket.jpg");
        game.setStockTotal(4);
        game.setPricePerDay(1800);
        gameRepository.save(game);

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ticket to Ride"));
    }
}
