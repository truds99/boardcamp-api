package com.boardcamp.api.services;

import com.boardcamp.api.exceptions.GameAlreadyExistsException;
import com.boardcamp.api.models.Game;
import com.boardcamp.api.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GamesServiceTest {

    private GamesService gamesService;
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gamesService = new GamesService(gameRepository);
    }

    @Test
    void shouldThrowWhenGameNameAlreadyExists() {
        Game existingGame = new Game();
        existingGame.setName("Banco Imobiliário");

        when(gameRepository.findByName("Banco Imobiliário"))
                .thenReturn(Optional.of(existingGame));

        Game newGame = new Game();
        newGame.setName("Banco Imobiliário");
        newGame.setImage("http://");
        newGame.setStockTotal(3);
        newGame.setPricePerDay(1500);

        assertThrows(GameAlreadyExistsException.class, () -> gamesService.createGame(newGame));
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {
        Game game = new Game();
        game.setName("");
        game.setImage("http://");
        game.setStockTotal(3);
        game.setPricePerDay(1500);

        assertThrows(IllegalArgumentException.class, () -> gamesService.createGame(game));
    }

    @Test
    void shouldCreateGameSuccessfully() {
        Game game = new Game();
        game.setName("Detetive");
        game.setImage("http://");
        game.setStockTotal(2);
        game.setPricePerDay(2500);

        when(gameRepository.findByName("Detetive")).thenReturn(Optional.empty());
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> {
            Game savedGame = invocation.getArgument(0);
            savedGame.setId(1L);
            return savedGame;
        });

        Game created = gamesService.createGame(game);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Detetive", created.getName());
        assertEquals(2, created.getStockTotal());
        assertEquals(2500, created.getPricePerDay());
    }
}
