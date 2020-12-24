package com.takeaway.got;

import com.takeaway.got.dto.CurrentPlayedDto;
import com.takeaway.got.model.GAMEMODE;
import com.takeaway.got.model.GAMETYPE;
import com.takeaway.got.model.Game;
import com.takeaway.got.model.Player;
import com.takeaway.got.repo.GameRepo;
import com.takeaway.got.repo.PlayerRepo;
import com.takeaway.got.service.GameService;
import com.takeaway.got.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
class GameOfThreeApplicationTests {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @MockBean
    private GameRepo gameRepo;

    @MockBean
    private PlayerRepo playerRepo;

    @Value("${gameofthree.fromPlayerId}")
    private String fromPlayer;

    @Value("${gameofthree.toPlayerId}")
    private String toPlayer;

    @Value("${gameofthree.game.mode}")
    private GAMEMODE gameMode;

    private CurrentPlayedDto currentPlayedDto;

    private UUID uuid;

    private Game game;

    private Player player;

    @BeforeEach
    void init() {
        uuid = UUID.randomUUID();

        currentPlayedDto = new CurrentPlayedDto();
        currentPlayedDto.setGameId(uuid);
        currentPlayedDto.setFromPlayer(fromPlayer);
        currentPlayedDto.setToPlayer(toPlayer);
        currentPlayedDto.setNumber(5);

        game = new Game(uuid, fromPlayer, toPlayer, null, 5, GAMETYPE.ACTIVE, null);
        player = new Player();
        player.setMode(gameMode);
        player.setPlayerId(fromPlayer);
        player.getGames().add(game);
    }

    @Test
    public void startGameShouldReturnANewGameIDTest() {
        Mockito.when(playerRepo.findById(fromPlayer))
                .thenReturn(Optional.of(player));
        assertNotNull(gameService.startGame(currentPlayedDto).getGameId());
    }

    @Test
    public void calculateCurrentNumberTest() {

        assertEquals(12, gameService.calculateNextMove(37));
    }

    @Test
    public void changeModeShouldUpdateModeTest() {
        Mockito.when(playerRepo.findById(fromPlayer))
                .thenReturn(Optional.of(player));
        assertEquals(GAMEMODE.MANUAL, playerService.changeMode("Manual"));
    }

    @Test
    public void createPlayerShouldReturnAnewPlayerTest() {
        Mockito.when(playerRepo.saveAndFlush(player))
                .thenReturn(player);
        assertNotNull(playerService.createPlayer());
    }

    @Test
    public void fetchPendingGamesReturnsAListTest() {
        Mockito.when(playerRepo.findById(fromPlayer))
                .thenReturn(Optional.of(player));

        assertNotNull(playerService.fetchPendingGames().size());
    }
}
