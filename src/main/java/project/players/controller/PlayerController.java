package project.players.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.players.entity.Player;
import project.players.service.PlayerService;
import project.players.exception.ResourceAlreadyExistException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
@Validated
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    ResponseEntity<String> addPlayer(@Valid @RequestBody Player player) throws ResourceAlreadyExistException {
        playerService.addPlayer(player);
        return new ResponseEntity<>("Player " + player.getUsername() + " is added successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = playerService.getPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping({"/{playerId}"})
    public ResponseEntity<Player> getPlayer(@PathVariable Integer playerId) {
        Optional<Player> player = playerService.getPlayerById(playerId);
        return player.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping({"/{playerId}"})
    public ResponseEntity<Player> updatePlayer(@PathVariable("playerId") Integer playerId,@Valid @RequestBody Player player) {
        playerService.updatePlayer(playerId, player);
        Optional<Player> playerDb = playerService.getPlayerById(playerId);
        return playerDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/{playerId}"})
    public ResponseEntity<Player> deletePlayer(@PathVariable("playerId") Integer playerId) {
        Optional<Player> playerDb = playerService.getPlayerById(playerId);
        playerService.deletePlayer(playerId);
        return playerDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
