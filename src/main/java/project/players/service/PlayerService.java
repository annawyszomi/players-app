package project.players.service;

import project.players.entity.Player;
import project.players.exception.ResourceAlreadyExistException;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    Player addPlayer(Player player) throws ResourceAlreadyExistException;

    List<Player> getPlayers();

    Optional<Player> getPlayerById(Integer id);

    Player updatePlayer(Integer id, Player player);

    void deletePlayer(Integer id);
}
