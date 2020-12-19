package com.takeaway.got.dto;

import java.util.UUID;

public class CurrentPlayedDto {

    private UUID gameId;
    private String toPlayer;
    private String fromPlayer;
    private int number;


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
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getFromPlayer() {
        return fromPlayer;
    }
    public void setFromPlayer(String fromPlayer) {
        this.fromPlayer = fromPlayer;
    }

    @Override
    public String toString() {
        return "CurrentPlayedDto [number=" + number + "]";
    }
}
