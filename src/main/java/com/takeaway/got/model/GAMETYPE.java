package com.takeaway.got.model;

public enum GAMETYPE {
	
	ACTIVE ("ACTIVE"),
	PENDING ("PENDING"),
	COMPLETE ("COMPLETE");
	
	private final String gameType;
		
	GAMETYPE(String gameType){
		this.gameType = gameType;
	}

	public String getGameType() {
		return gameType;
	}
}
