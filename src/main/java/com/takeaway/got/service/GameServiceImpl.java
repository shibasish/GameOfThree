package com.takeaway.got.service;

import com.takeaway.got.dto.CurrentPlayedDto;
import com.takeaway.got.gateway.IntegrationGateway;
import com.takeaway.got.model.Game;
import com.takeaway.got.model.Player;
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

@Service
public class GameServiceImpl implements GameService {
	@Autowired
	IntegrationGateway integrationGateway;

	@Autowired
	GameRepo gameRepo;

	@Autowired
	PlayerRepo playerRepo;

	@Value("${gameofthree.fromPlayerId}")
	private String fromPlayer;

	@Transactional
	public String startGame(CurrentPlayedDto currentPlayedDto) {

		UUID uuid = UUID.randomUUID();
		int startingNumber = new Random().nextInt(10000);

		Optional<Player> player = playerRepo.findById(fromPlayer);
		player.ifPresentOrElse(currentPlayer -> {

			gameRepo.save(createGame(startingNumber, uuid, currentPlayedDto, "Active"));

			List<Game> games = new ArrayList<>();

			currentPlayer.setGames(games);
			playerRepo.save(currentPlayer);

			currentPlayedDto.setNumber(startingNumber);
			currentPlayedDto.setGameId(uuid);

			sendToChannel(currentPlayedDto);

		}, () -> {

			Player newPlayer = new Player();
			newPlayer.setPlayerId(fromPlayer);
			newPlayer.setMode("automatic");

			gameRepo.save(createGame(startingNumber, uuid, currentPlayedDto, "Active"));

			List<Game> games = new ArrayList<>();
			newPlayer.setGames(games);

			playerRepo.save(newPlayer);

			currentPlayedDto.setNumber(startingNumber);
			currentPlayedDto.setGameId(uuid);

			sendToChannel(currentPlayedDto);
		});

		return "Played Successfully";
	}

	
	@Transactional
	public String playTurn(CurrentPlayedDto currentPlayedDto) {

		if (currentPlayedDto.getNumber() == 0) {
			System.out.println("received 0 from P1");
			return "You Lost!!";
		}

		Optional<Player> player = playerRepo.findById(fromPlayer);
		Optional<Game> game = player.get().getGames().stream()
				.filter(currentGame -> currentGame.getGameId() == currentPlayedDto.getGameId()).findFirst();

		final int val = calculateNextMove(currentPlayedDto.getNumber());

		game.ifPresentOrElse(currentGame -> {

			if (!checkWon(val, currentGame, currentPlayedDto)) {

				currentGame.setCurrentNumber(val);

				currentPlayedDto.setNumber(val);
				currentPlayedDto.setToPlayer(currentPlayedDto.getFromPlayer());
				currentPlayedDto.setFromPlayer(fromPlayer);

				sendToChannel(currentPlayedDto);
			}
		}, () -> {

			Game newGame = createGame(val, currentPlayedDto.getGameId(), currentPlayedDto, "Active");

			player.get().getGames().add(newGame);

			gameRepo.save(newGame);
			playerRepo.save(player.get());

			if (!checkWon(val, newGame, currentPlayedDto)) {

				currentPlayedDto.setToPlayer(currentPlayedDto.getFromPlayer());
				currentPlayedDto.setFromPlayer(fromPlayer);
				currentPlayedDto.setNumber(val);

				sendToChannel(currentPlayedDto);
			}
		});
		return "Successfully played!";
	}

	private boolean checkWon(int currentNumber, Game game, CurrentPlayedDto currentPlayedDto) {

		if (currentNumber == 1) {
			System.out.println("You Won!!");

			game.setStatus("COMPLETED");
			game.setCurrentNumber(0);

			currentPlayedDto.setToPlayer(currentPlayedDto.getFromPlayer());
			currentPlayedDto.setFromPlayer(fromPlayer);
			currentPlayedDto.setNumber(0);

			sendToChannel(currentPlayedDto);
			return true;
		}
		return false;
	}

	public boolean alreadyPlayed(String fromPlayer, String toPlayer) {

		return false;
	}

	private int calculateNextMove(int playedNumber) {

		if (playedNumber % 3 == 0) {
			int nextVal = playedNumber / 3;
			if (nextVal == 1) {
				return 1;
			}
			System.out.println("nextval: " + nextVal);
			return nextVal;
		} else if (playedNumber % 3 == 1) {
			int nextVal = (playedNumber - 1) / 3;
			if (nextVal == 1) {
				return 1;
			}
			System.out.println("nextval: " + nextVal);
			return nextVal;
		} else if (playedNumber % 3 == 2) {
			int nextVal = (playedNumber + 1) / 3;
			if (nextVal == 1) {
				return 1;
			}
			System.out.println("nextval: " + nextVal);
			return nextVal;
		}

		return 0;
	}
	
	private void sendToChannel(CurrentPlayedDto currentPlayedDto) {
		Message<CurrentPlayedDto> message = new GenericMessage<CurrentPlayedDto>(currentPlayedDto);
		this.integrationGateway.sendToMqtt(message);
	}

	private Game createGame(int startingNumber, UUID uuid, CurrentPlayedDto currentPlayedDto, String status) {

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
