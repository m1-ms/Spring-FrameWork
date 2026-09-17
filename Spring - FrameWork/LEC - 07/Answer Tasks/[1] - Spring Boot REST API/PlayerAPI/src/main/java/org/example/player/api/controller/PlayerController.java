package org.example.player.api.controller;

import org.example.player.api.model.Players;
import org.example.player.api.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // Save Player
    @PostMapping
    public ResponseEntity<Players> savePlayer(@RequestBody Players player) {
        Players savedPlayer = playerService.savePlayer(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
    }

    // Update Player
    @PutMapping("/{id}")
    public ResponseEntity<Players> updatePlayer(@PathVariable Long id, @RequestBody Players player) {
        Players updatedPlayer = playerService.updatePlayer(id, player);
        if (updatedPlayer != null) {
            return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get Player By ID
    @GetMapping("/{id}")
    public ResponseEntity<Players> getPlayerById(@PathVariable Long id) {
        Players player = playerService.getPlayerById(id);
        if (player != null) {
            return new ResponseEntity<>(player, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete Player
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable Long id) {
        Players player = playerService.getPlayerById(id);
        if (player != null) {
            playerService.deletePlayer(id);
            return new ResponseEntity<>("Player deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get All Players
    @GetMapping
    public ResponseEntity<List<Players>> getAllPlayers() {
        List<Players> players = playerService.getAllPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

}