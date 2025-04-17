package com.boardcamp.api.services;

import com.boardcamp.api.models.Game;
import com.boardcamp.api.repositories.GameRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class GamesServiceTest {

    private GameRepository gameRepository;
    private GamesService gamesService;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gamesService = new GamesService(gameRepository);
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {
        Game game = new Game();
        game.setName("");
        game.setImage("http://");
        game.setStockTotal(1);
        game.setPricePerDay(1000);

        assertThrows(IllegalArgumentException.class, () -> gamesService.createGame(game));
    }

    @Test
    void shouldThrowWhenGameAlreadyExists() {
        Game game = new Game();
        game.setName("Banco Imobiliário");
        game.setImage("http://");
        game.setStockTotal(1);
        game.setPricePerDay(1000);

        when(gameRepository.findByName("Banco Imobiliário"))
                .thenReturn(Optional.of(new Game()));

        assertThrows(RuntimeException.class, () -> gamesService.createGame(game));
    }

    @Test
    void shouldCreateGameSuccessfully() {
        Game game = new Game();
        game.setName("Detetive");
        game.setImage("http://");
        game.setStockTotal(2);
        game.setPricePerDay(1500);

        when(gameRepository.findByName("Detetive"))
                .thenReturn(Optional.empty());

        when(gameRepository.save(any(Game.class)))
                .thenAnswer(invocation -> {
                    Game saved = invocation.getArgument(0);
                    saved.setId(1L);
                    return saved;
                });

        Game result = gamesService.createGame(game);

        assertNotNull(result);
        assertEquals("Detetive", result.getName());
        assertEquals(1L, result.getId());
    }
}

