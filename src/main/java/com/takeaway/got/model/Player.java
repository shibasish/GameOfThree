package com.takeaway.got.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity(name="PLAYER")
public class Player {
	
	@Id
	@Column(name="PLAYER_ID")
	private String playerId;
	
	@Column(name="MODE")
	private String mode;
	
	@Column(name="GAME_ID")
	@OneToMany
	private List<Game> games = new ArrayList<>();

	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public List<Game> getGames() {
		return games;
	}
	public void setGames(List<Game> games) {
		this.games = games;
	}
}