package com.takeaway.got.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class Game {

    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID gameId;
    private String firstPlayer;
    private String secondPlayer;
    private String playerTurn;
    private int currentNumber;
    private String status;
    private String result;


    public UUID getGameId() {
        return gameId;
    }
    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }
    public String getFirstPlayer() {
        return firstPlayer;
    }
    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }
    public String getSecondPlayer() {
        return secondPlayer;
    }
    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer = secondPlayer;
    }
    public String getPlayerTurn() {
        return playerTurn;
    }
    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }
    public int getCurrentNumber() {
        return currentNumber;
    }
    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
}
