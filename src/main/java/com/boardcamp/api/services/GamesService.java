package com.boardcamp.api.services;

import com.boardcamp.api.exceptions.GameAlreadyExistsException;
import com.boardcamp.api.models.Game;
import com.boardcamp.api.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamesService {

    private final GameRepository gameRepository;

    public GamesService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> listGames() {
        return gameRepository.findAll();
    }

    public Game createGame(Game game) {
        if (game.getName() == null || game.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (game.getStockTotal() == null || game.getStockTotal() <= 0) {
            throw new IllegalArgumentException("Stock must be greater than 0.");
        }

        if (game.getPricePerDay() == null || game.getPricePerDay() <= 0) {
            throw new IllegalArgumentException("Price per day must be greater than 0.");
        }

        if (gameRepository.findByName(game.getName()).isPresent()) {
            throw new GameAlreadyExistsException("Game already exists.");

        }

        return gameRepository.save(game);
    }
}
