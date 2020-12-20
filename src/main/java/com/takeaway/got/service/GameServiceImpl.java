package com.takeaway.got.service;

import com.takeaway.got.dto.CurrentPlayedDto;
import com.takeaway.got.gateway.IntegrationGateway;
import com.takeaway.got.model.Game;
import com.takeaway.got.model.Player;
import com.takeaway.got.model.GAMEMODE;
import com.takeaway.got.model.GAMETYPE;
import com.takeaway.got.repo.GameRepo;
import com.takeaway.got.repo.PlayerRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	IntegrationGateway integrationGateway;

	@Autowired
	GameRepo gameRepo;

	@Autowired
	PlayerRepo playerRepo;

	@Autowired
	PlayerService playerService;

	@Value("${gameofthree.fromPlayerId}")
	private String fromPlayer;

	public String startGame(CurrentPlayedDto currentPlayedDto) {

		UUID uuid = UUID.randomUUID();
		int startingNumber = new Random().nextInt(10000);

		Optional<Player> player = playerRepo.findById(fromPlayer);
		player.ifPresentOrElse(currentPlayer -> {

			persistGame(currentPlayer, startingNumber, uuid, currentPlayedDto);

			currentPlayedDto.setNumber(startingNumber);
			currentPlayedDto.setGameId(uuid);

			sendToChannel(currentPlayedDto);

		}, () -> {
			
			Player newPlayer = playerService.createPlayer(GAMEMODE.AUTOMATIC);

			persistGame(newPlayer, startingNumber, uuid, currentPlayedDto);

			currentPlayedDto.setNumber(startingNumber);
			currentPlayedDto.setGameId(uuid);

			sendToChannel(currentPlayedDto);
		});

		return "Played Successfully";
	}
	
	
	@Transactional
	public String playTurn(CurrentPlayedDto currentPlayedDto) {

		Optional<Player> player = playerRepo.findById(fromPlayer);
		
		Predicate<Game> activeGames =  currentGame -> currentGame.getStatus() == GAMETYPE.ACTIVE;
		Predicate<Game> pendingGames =  currentGame -> currentGame.getStatus() == GAMETYPE.PENDING;

		Optional<Game> game = player.get().getGames().stream()
				.filter(currentGame -> currentGame.getGameId().equals(currentPlayedDto.getGameId()))
				.filter(activeGames.or(pendingGames))
				.findFirst();

		game.ifPresent(currentGame -> {
			
			if(player.get().getMode() == GAMEMODE.MANUAL) {
				currentGame.setCurrentNumber(currentPlayedDto.getNumber());
				currentGame.setPlayerTurn(currentPlayedDto.getToPlayer());
				currentGame.setStatus(GAMETYPE.PENDING);
				gameRepo.save(currentGame);
				
				// logic for user notification goes here
			}
			else {
				playAutomatic(currentPlayedDto, currentGame);
			}
		});

		return "Successfully played!";
	}
	
	@Transactional
	private void playAutomatic(CurrentPlayedDto currentPlayedDto, Game game) {
		int nextValue = calculateNextMove(currentPlayedDto.getNumber());

		if (!checkWon(nextValue, game, currentPlayedDto)) {
			currentPlayedDto.setNumber(nextValue);
			play(game, currentPlayedDto);
		}
	}
	
	@Transactional
	public String playManual(CurrentPlayedDto currentPlayedDto) {

		Optional<Game> currentGame = playerRepo.findById(fromPlayer).get().getGames().stream()
				// check that gameid is present or else throw exception
				.filter(game -> game.getGameId().equals(currentPlayedDto.getGameId()))
				.filter(game -> game.getStatus() == GAMETYPE.PENDING)
				.filter(game -> game.getPlayerTurn().equalsIgnoreCase(fromPlayer))
				.findFirst();

		currentGame.ifPresent(game -> {
			
			if (!checkWon(currentPlayedDto.getNumber(), game, currentPlayedDto))
				play(game, currentPlayedDto);
		});
		return "Played Successfully";
	}

	@Transactional
	private void persistGame(Player player, int number, UUID uuid, CurrentPlayedDto currentPlayedDto) {

		Game newGame = createGame(number, uuid, currentPlayedDto, GAMETYPE.ACTIVE);
		gameRepo.save(newGame);

		player.getGames().add(newGame);
		playerRepo.save(player);
	}
	
	@Transactional
	private void play(Game game, CurrentPlayedDto currentPlayedDto) {
		game.setPlayerTurn(currentPlayedDto.getFromPlayer());
		game.setCurrentNumber(currentPlayedDto.getNumber());

		gameRepo.save(game);

		currentPlayedDto.setToPlayer(currentPlayedDto.getFromPlayer());
		currentPlayedDto.setFromPlayer(fromPlayer);

		sendToChannel(currentPlayedDto);
	}

	private boolean checkWon(int currentNumber, Game game, CurrentPlayedDto currentPlayedDto) {

		if (currentNumber <= 1) {
			System.out.println("You Won!!");

			game.setStatus(GAMETYPE.COMPLETE);
			game.setCurrentNumber(0);

			gameRepo.save(game);

			currentPlayedDto.setToPlayer(currentPlayedDto.getFromPlayer());
			currentPlayedDto.setFromPlayer(fromPlayer);
			currentPlayedDto.setNumber(0);

			sendToChannel(currentPlayedDto);
			return true;
		}
		return false;
	}

	private int calculateNextMove(int playedNumber) {

		int modulo = playedNumber % 3;
		int nextMove = 0;

		switch (modulo) {
		case 0:
			nextMove = (playedNumber / 3);
			break;
		case 1:
			nextMove = ((playedNumber - 1) / 3);
			break;
		case 2:
			nextMove = ((playedNumber + 1) / 3);
			break;
		default:
			nextMove = 0;
			break;
		}

		return nextMove;

	}

	private void sendToChannel(CurrentPlayedDto currentPlayedDto) {
		Message<CurrentPlayedDto> message = new GenericMessage<CurrentPlayedDto>(currentPlayedDto);
		this.integrationGateway.sendToMqtt(message);
	}

	private Game createGame(int startingNumber, UUID uuid, CurrentPlayedDto currentPlayedDto, GAMETYPE status) {

		Game game = new Game();
		game.setCurrentNumber(startingNumber);
		game.setFirstPlayer(fromPlayer);
		game.setGameId(uuid);
		game.setPlayerTurn(currentPlayedDto.getToPlayer());
		game.setSecondPlayer(currentPlayedDto.getToPlayer());
		game.setStatus(status);

		return game;
	}

}
