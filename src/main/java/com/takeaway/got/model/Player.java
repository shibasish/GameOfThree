package com.takeaway.got.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity(name="PLAYER")
public class Player {
	
	@Id
	@Column(name="PLAYER_ID")
	private String playerId;
	
	@Column(name="MODE")
	@Enumerated(EnumType.STRING) 
	private GAMEMODE mode;
	
	@Column(name="PLAYER_GAME")
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
	 						CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
	private List<Game> games = new ArrayList<>();

	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public GAMEMODE getMode() {
		return mode;
	}
	public void setMode(GAMEMODE mode) {
		this.mode = mode;
	}
	public List<Game> getGames() {
		return games;
	}
	public void setGames(List<Game> games) {
		this.games = games;
	}
}