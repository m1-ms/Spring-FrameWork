package org.example.player.api.service;

import org.example.player.api.model.Players;

import java.util.List;

public interface PlayerService {

    Players savePlayer(Players player);

    Players updatePlayer(Long id, Players playerDetails);

    Players getPlayerById(Long id);

    void deletePlayer(Long id);

    List<Players> getAllPlayers();

}