package com.boardcamp.api.controllers;

import com.boardcamp.api.models.Game;
import com.boardcamp.api.services.GamesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

    private GamesService gameService;
    private GamesController gameController;

    @BeforeEach
    void setUp() {
        gameService = mock(GamesService.class);
        gameController = new GamesController(gameService);
    }

    @Test
    void shouldReturnListOfGames() {
        Game game = new Game();
        game.setName("FIFA");
        game.setImage("http://image.com");
        game.setStockTotal(10);
        game.setPricePerDay(500);

        when(gameService.listGames()).thenReturn(List.of(game));

        ResponseEntity<List<Game>> response = gameController.getGames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("FIFA", response.getBody().get(0).getName());
    }
}
