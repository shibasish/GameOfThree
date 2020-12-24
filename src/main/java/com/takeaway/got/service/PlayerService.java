package com.takeaway.got.service;

import java.util.List;
import java.util.Optional;

import com.takeaway.got.dto.GamesDto;
import com.takeaway.got.dto.PendingGameDto;
import com.takeaway.got.model.GAMEMODE;
import com.takeaway.got.model.Player;

public interface PlayerService {
	GAMEMODE changeMode(String gamemode);
	Optional<Player> createPlayer();
	List<PendingGameDto> fetchPendingGames();
	List<GamesDto> fetchAllGames();
}
