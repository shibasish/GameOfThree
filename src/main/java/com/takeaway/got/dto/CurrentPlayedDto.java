package com.takeaway.got.dto;

public class CurrentPlayedDto {

    private String toPlayer;
    private String fromPlayer;
    private int number;

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
