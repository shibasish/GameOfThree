package com.takeaway.got.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity(name = "GAME")
public class Game {

    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "GAME_ID")
    private UUID gameId;
    
    @Column(name = "FIRST_PLAYER")
    private String firstPlayer;
    
    @Column(name = "SECOND_PLAYERD")
    private String secondPlayer;
    
    @Column(name = "PLAYER_TURN")
    private String playerTurn;
    
    @Column(name = "CURRENT_NUMBER")
    private int currentNumber;
    
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING) 
    private GAMETYPE status;
    
    @Column(name = "RESULT")
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
    public GAMETYPE getStatus() {
        return status;
    }
    public void setStatus(GAMETYPE status) {
        this.status = status;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
}
