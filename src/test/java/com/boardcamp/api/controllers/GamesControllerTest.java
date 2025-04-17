package com.boardcamp.api.controllers;

import com.boardcamp.api.models.Game;
import com.boardcamp.api.services.GamesService;
import com.boardcamp.api.exceptions.GameAlreadyExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void shouldCreateGame() {
        Game game = new Game();
        game.setName("FIFA");
        game.setImage("http://image.com");
        game.setStockTotal(10);
        game.setPricePerDay(500);

        when(gameService.createGame(any(Game.class))).thenReturn(game);

        ResponseEntity<Game> response = gameController.createGame(game);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("FIFA", response.getBody().getName());
    }

    @Test
    void shouldReturn409IfGameAlreadyExists() {
        Game game = new Game();
        game.setName("FIFA");
        game.setImage("http://image.com");
        game.setStockTotal(10);
        game.setPricePerDay(500);

        when(gameService.createGame(any(Game.class)))
            .thenThrow(new GameAlreadyExistsException("Game already exists."));

        ResponseEntity<Game> response = gameController.createGame(game);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldReturn400IfNameIsEmpty() {
        Game game = new Game();
        game.setName("");
        game.setImage("http://image.com");
        game.setStockTotal(10);
        game.setPricePerDay(500);

        when(gameService.createGame(any(Game.class)))
            .thenThrow(new IllegalArgumentException("Name is required"));

        ResponseEntity<Game> response = gameController.createGame(game);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturn201WhenGameIsCreated() {
        Game game = new Game();
        game.setName("FIFA");
        game.setImage("http://image.com");
        game.setStockTotal(10);
        game.setPricePerDay(500);

        when(gameService.createGame(any(Game.class)))
            .thenReturn(game);

        ResponseEntity<Game> response = gameController.createGame(game);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("FIFA", response.getBody().getName());
        assertEquals(10, response.getBody().getStockTotal());
        assertEquals(500, response.getBody().getPricePerDay());
    }
}
