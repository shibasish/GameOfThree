package com.takeaway.got.service;

import com.takeaway.got.dto.CurrentPlayedDto;
import com.takeaway.got.dto.ResponseDto;
import com.takeaway.got.exception.ResourceNotFoundException;
import com.takeaway.got.exception.ValueMisingException;
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

	@Value("${gameofthree.toPlayerId}")
	private String toPlayer;

	@Value("${gameofthree.game.mode}")
	private GAMEMODE gameMode;

	@Override
	public ResponseDto startGame(CurrentPlayedDto currentPlayedDto) {

		UUID uuid = UUID.randomUUID();
		int startingNumber = new Random().nextInt(100);

		currentPlayedDto.setFromPlayer(fromPlayer);
		currentPlayedDto.setNumber(startingNumber);
		currentPlayedDto.setGameId(uuid);
		currentPlayedDto.setToPlayer(toPlayer);

		Game game = persistGame(currentPlayedDto);

		sendToChannel(currentPlayedDto);

		return new ResponseDto(uuid, "Played Successfully", startingNumber, game.getStatus());
	}

	@Transactional
	public ResponseDto playTurn(CurrentPlayedDto currentPlayedDto) {

		if (checkAlreadyPlayed(currentPlayedDto))
			return null;

		Player player = playerRepo.findById(fromPlayer)
				.orElseThrow(() -> new ResourceNotFoundException("Player " + fromPlayer + " not found"));

		Predicate<Game> activeGames = currentGame -> currentGame.getStatus() == GAMETYPE.ACTIVE;
		Predicate<Game> pendingGames = currentGame -> currentGame.getStatus() == GAMETYPE.PENDING;

		Game game = player.getGames().stream()
				.filter(currentGame -> currentGame.getGameId().equals(currentPlayedDto.getGameId()))
				.filter(activeGames.or(pendingGames))
				.findFirst()
				.orElseGet(()-> persistGame(currentPlayedDto));

		if (currentPlayedDto.getNumber() == 0 || currentPlayedDto.getNumber() == 1) {
			game.setCurrentNumber(0);
			game.setResult("LOST");
			game.setStatus(GAMETYPE.COMPLETE);

			gameRepo.save(game);
			return new ResponseDto(game.getGameId(), "Game lost", game.getCurrentNumber(), game.getStatus());
		}

		if (player.getMode() == GAMEMODE.MANUAL) {

			game.setStatus(GAMETYPE.PENDING);
			game.setCurrentNumber(currentPlayedDto.getNumber());
			game.setPlayerTurn(currentPlayedDto.getToPlayer());
			gameRepo.save(game);

			// TODO: logic for user notification goes here --> workers

			return new ResponseDto(game.getGameId(), "Game is Pending", game.getCurrentNumber(), game.getStatus());
		} else
			return playAutomatic(currentPlayedDto, game);
	}

	private boolean checkAlreadyPlayed(CurrentPlayedDto currentPlayedDto) {
		if (currentPlayedDto.getFromPlayer() == null)
			currentPlayedDto.setFromPlayer(fromPlayer);
		return currentPlayedDto.getFromPlayer().equalsIgnoreCase(fromPlayer) ? true : false;
	}

	@Transactional
	private ResponseDto playAutomatic(CurrentPlayedDto currentPlayedDto, Game game) {
		int nextValue = calculateNextMove(currentPlayedDto.getNumber());
		if (!checkWon(nextValue, game, currentPlayedDto)) {
			currentPlayedDto.setNumber(nextValue);
			currentPlayedDto.setToPlayer(currentPlayedDto.getFromPlayer());
			game.setPlayerTurn(currentPlayedDto.getFromPlayer());
			play(game, currentPlayedDto);
			return new ResponseDto(game.getGameId(), "Played Successfully.", currentPlayedDto.getNumber(), game.getStatus());
		}

		game.setStatus(GAMETYPE.COMPLETE);
		game.setResult("WINNER");
		gameRepo.save(game);

		return new ResponseDto(game.getGameId(), "You won!!", game.getCurrentNumber(), game.getStatus());
	}

	@Transactional
	public ResponseDto playManual(CurrentPlayedDto currentPlayedDto) {

		if (currentPlayedDto.getNumber() == 0 || currentPlayedDto.getToPlayer() == null
				|| currentPlayedDto.getGameId() == null)
			throw new ValueMisingException("number or toPlayer or gameid is missing");

		Game currentGame = playerRepo.findById(fromPlayer).get().getGames().stream()
				.filter(game -> game.getGameId().equals(currentPlayedDto.getGameId()))
				.filter(game -> game.getStatus() == GAMETYPE.PENDING)
				.filter(game -> game.getPlayerTurn().equalsIgnoreCase(fromPlayer)).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Game not found / already played"));

		if (!checkWon(currentPlayedDto.getNumber(), currentGame, currentPlayedDto)) {
			currentGame.setPlayerTurn(currentPlayedDto.getToPlayer());
			play(currentGame, currentPlayedDto);
		} else {
			currentGame.setStatus(GAMETYPE.COMPLETE);
			currentGame.setResult("WINNER");
			gameRepo.save(currentGame);
			return new ResponseDto(currentGame.getGameId(), "You won!", 0, currentGame.getStatus());
		}

		return new ResponseDto(currentGame.getGameId(), "Played Successfully", 0, currentGame.getStatus());
	}

	@Transactional
	private Game persistGame(CurrentPlayedDto currentPlayedDto) {

		Player player = playerRepo.findById(fromPlayer)
				.orElseThrow(() -> new ResourceNotFoundException("Player "+ fromPlayer + " not found" ));

		Game newGame = createGame(currentPlayedDto, GAMETYPE.ACTIVE);
		gameRepo.save(newGame);

		player.getGames().add(newGame);

		playerRepo.save(player);

		return newGame;
	}

	private void sendToChannel(CurrentPlayedDto currentPlayedDto) {
		Message<CurrentPlayedDto> message = new GenericMessage<CurrentPlayedDto>(currentPlayedDto);
		this.integrationGateway.sendToMqtt(message);
	}

	private Game createGame(CurrentPlayedDto currentPlayedDto, GAMETYPE status) {

		Game game = new Game();
		game.setCurrentNumber(currentPlayedDto.getNumber());
		game.setFirstPlayer(fromPlayer);
		game.setGameId(currentPlayedDto.getGameId());
		game.setPlayerTurn(currentPlayedDto.getToPlayer());
		game.setSecondPlayer(currentPlayedDto.getToPlayer());
		game.setStatus(status);

		return game;
	}

	@Transactional
	private void play(Game game, CurrentPlayedDto currentPlayedDto) {
		game.setCurrentNumber(currentPlayedDto.getNumber());
		gameRepo.save(game);

		currentPlayedDto.setFromPlayer(fromPlayer);
		sendToChannel(currentPlayedDto);
	}

	private boolean checkWon(int currentNumber, Game game, CurrentPlayedDto currentPlayedDto) {

		if (currentNumber <= 1) {
			game.setStatus(GAMETYPE.COMPLETE);
			game.setResult("WINNER");
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

	public int calculateNextMove(int playedNumber) {

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

}
