package com.takeaway.got.dto;

import java.util.UUID;

public class GamesDto {

    private UUID gameId;
    private String toPlayer;
    private String result;

    public GamesDto(UUID gameId, String toPlayer, String result) {
        this.gameId = gameId;
        this.toPlayer = toPlayer;
        this.result = result;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public String getToPlayer() {
        return toPlayer;
    }

    public void setToPlayer(String toPlayer) {
        this.toPlayer = toPlayer;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
