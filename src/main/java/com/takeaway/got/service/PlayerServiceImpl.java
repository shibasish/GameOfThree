package com.takeaway.got.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	
	@Override
	@Transactional
	public String changeMode(String gamemode) {
		
		Optional<Player> player = playerRepo.findById(fromPlayer);
		
		player.ifPresent( currentPlayer -> {
			if(gamemode == null)
				currentPlayer.setMode(GAMEMODE.AUTOMATIC);
			else
				currentPlayer.setMode(parseStringToEnumMode(gamemode));
			
			playerRepo.save(currentPlayer);
		});
		return "Mode updated";
	}
	
	@Override
	public Player createPlayer(GAMEMODE gamemode) {
		
		Player newPlayer = new Player();
		newPlayer.setPlayerId(fromPlayer);
		newPlayer.setMode(gamemode);
		
		return newPlayer;
	}

	@Override
	@Transactional
	public List<PendingGameDto> fetchPendingGames() {
		return playerRepo.findById(fromPlayer).get().getGames().stream()
					.filter( game -> game.getStatus() == GAMETYPE.PENDING )
					.filter( game -> game.getPlayerTurn().equalsIgnoreCase(fromPlayer))
					.map( game -> new PendingGameDto(game.getSecondPlayer(), game.getCurrentNumber(), game.getGameId()))
					.collect(Collectors.toList());
	}
	
	private GAMEMODE parseStringToEnumMode(String mode) {
		return GAMEMODE.valueOf(mode.toUpperCase());
	}
}
