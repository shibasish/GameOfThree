package com.takeaway.got.dto;

import com.takeaway.got.model.GAMEMODE;
import com.takeaway.got.model.GAMETYPE;

import java.util.UUID;

public class ResponseDto {

    private UUID gameId;
    private String status;
    private int number;
    private GAMETYPE type;

    public ResponseDto(UUID gameId, String status, int number, GAMETYPE type) {
        this.gameId = gameId;
        this.status = status;
        this.number = number;
        this.type = type;
    }

    public UUID getGameId() {
        return gameId;
    }
    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public GAMETYPE getType() {
        return type;
    }
    public void setType(GAMETYPE type) {
        this.type = type;
    }
}
