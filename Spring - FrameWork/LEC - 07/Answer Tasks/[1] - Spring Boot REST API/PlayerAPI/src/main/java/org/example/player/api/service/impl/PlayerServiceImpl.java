package org.example.player.api.service.impl;

import org.example.player.api.model.Players;
import org.example.player.api.repository.PlayerRepository;
import org.example.player.api.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Players savePlayer(Players player) {
        return playerRepository.save(player);
    }

    @Override
    public Players updatePlayer(Long id, Players playerDetails) {
        Players player = playerRepository.findById(id).orElse(null);

        if (player != null) {
            player.setName(playerDetails.getName());
            player.setAge(playerDetails.getAge());
            player.setTeam(playerDetails.getTeam());
            return playerRepository.save(player);
        }
        return null;
    }

    @Override
    public Players getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public List<Players> getAllPlayers() {
        return playerRepository.findAll();
    }
}