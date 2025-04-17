package com.boardcamp.api.controllers;

import com.boardcamp.api.models.Game;
import com.boardcamp.api.services.GamesService;
import com.boardcamp.api.exceptions.GameAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GamesController {

    private final GamesService gamesService;

    public GamesController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @GetMapping
    public ResponseEntity<List<Game>> getGames() {
        List<Game> games = gamesService.listGames();
        return ResponseEntity.ok(games);
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        try {
            Game createdGame = gamesService.createGame(game);
            return ResponseEntity.status(201).body(createdGame);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400
        } catch (GameAlreadyExistsException e) {
            return ResponseEntity.status(409).build(); // 409
        }
    }
}
