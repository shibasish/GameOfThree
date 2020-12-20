package com.takeaway.got.model;

public enum GAMEMODE {
	
	AUTOMATIC ("AUTOMATIC"),
	MANUAL ("MANUAL");
	
	private final String gameMode;
		
	GAMEMODE(String gameMode){
		this.gameMode = gameMode;
	}

	public String getGameMode() {
		return gameMode;
	}

}
