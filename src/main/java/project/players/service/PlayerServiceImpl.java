package project.players.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.players.entity.Player;
import project.players.exception.ResourceAlreadyExistException;
import project.players.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;


    @Override
    public Player addPlayer(Player player) throws ResourceAlreadyExistException {
        Optional<Player> savedPlayer = playerRepository.findByEmail(player.getEmail());
        if (savedPlayer.isPresent()) {
            throw new ResourceAlreadyExistException("This email already exist in db:" + player.getEmail());
        }
        return playerRepository.save(player);
    }

    @Override
    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }


    @Override
    public Optional<Player> getPlayerById(Integer id) {
        return playerRepository.findById(id);
    }


    @Override
    public Player updatePlayer(Integer id, Player newPlayer) {
        return playerRepository.findById(id)
                .map(player -> {
                    player.updatePlayer(newPlayer);
                    return playerRepository.save(player);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found"));
    }

    @Override
    public void deletePlayer(Integer id) {
        playerRepository.deleteById(id);
    }
}
