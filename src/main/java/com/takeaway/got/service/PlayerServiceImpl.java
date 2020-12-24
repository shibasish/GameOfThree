package com.takeaway.got.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.takeaway.got.dto.GamesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.takeaway.got.dto.PendingGameDto;
import com.takeaway.got.model.GAMEMODE;
import com.takeaway.got.model.GAMETYPE;
import com.takeaway.got.model.Player;
import com.takeaway.got.repo.PlayerRepo;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepo playerRepo;

    @Value("${gameofthree.fromPlayerId}")
    private String fromPlayer;

    @Value("${gameofthree.game.mode}")
    private String gameMode;

    @Override
    @Transactional
    public GAMEMODE changeMode(String gamemode) {

        Optional<Player> player = playerRepo.findById(fromPlayer);
        GAMEMODE mode = parseStringToEnumMode(gamemode);
        player.ifPresent(currentPlayer -> {
            currentPlayer.setMode(mode);
            playerRepo.save(currentPlayer);
        });
        return mode;
    }

    @Override
    @Transactional
    public Optional<Player> createPlayer() {
        return playerRepo.findById(fromPlayer)
                .or(() -> {
                    Player newPlayer = new Player();
                    newPlayer.setPlayerId(fromPlayer);

                    if (gameMode.equalsIgnoreCase(GAMEMODE.AUTOMATIC.getGameMode()))
                        newPlayer.setMode(GAMEMODE.AUTOMATIC);
                    else
                        newPlayer.setMode(GAMEMODE.MANUAL);

                    playerRepo.saveAndFlush(newPlayer);

                    return Optional.of(newPlayer);
                });
    }

    @Override
    @Transactional
    public List<PendingGameDto> fetchPendingGames() {
        return playerRepo.findById(fromPlayer).get().getGames().stream()
                .filter(game -> game.getStatus() == GAMETYPE.PENDING)
                .filter(game -> game.getPlayerTurn().equalsIgnoreCase(fromPlayer))
                .map(game -> new PendingGameDto(game.getSecondPlayer(), game.getCurrentNumber(), game.getGameId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<GamesDto> fetchAllGames() {
        return playerRepo.findById(fromPlayer).get().getGames().stream()
                .map( game -> new GamesDto(game.getGameId(), game.getSecondPlayer(), game.getResult()) )
                .collect(Collectors.toList());
    }

    private GAMEMODE parseStringToEnumMode(String mode) {
        return GAMEMODE.valueOf(mode.toUpperCase());
    }
}
