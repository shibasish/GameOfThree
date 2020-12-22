package com.takeaway.got.service;

import java.util.List;

import com.takeaway.got.dto.PendingGameDto;
import com.takeaway.got.model.GAMEMODE;
import com.takeaway.got.model.Player;

public interface PlayerService {
	String changeMode(String gamemode);
	Player createPlayer();
	List<PendingGameDto> fetchPendingGames();
}
