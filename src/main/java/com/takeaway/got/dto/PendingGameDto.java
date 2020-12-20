package com.takeaway.got.dto;

import java.util.UUID;

public class PendingGameDto {
	
	private String playerName;
	private int playerNumber;
	private UUID gameId;
	
	
	public PendingGameDto(String playerName, int playerNumber, UUID uuid) {
		super();
		this.playerName = playerName;
		this.playerNumber = playerNumber;
		this.gameId = uuid;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getPlayerNumber() {
		return playerNumber;
	}
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public UUID getGameId() {
		return gameId;
	}

	public void setGameId(UUID gameId) {
		this.gameId = gameId;
	}
}