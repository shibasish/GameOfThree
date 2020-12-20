package com.takeaway.got.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.takeaway.got.dto.PendingGameDto;
import com.takeaway.got.model.GAMEMODE;
import com.takeaway.got.service.PlayerService;

@RestController
@RequestMapping("/api/player/details")
public class PlayerDetailsController {
	
	@Autowired
	PlayerService playerService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/pending")
	public ResponseEntity<List<PendingGameDto>> getPendingGames() {
		return ResponseEntity.ok().body(playerService.fetchPendingGames());
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/mode/{mode}")
	public ResponseEntity<String> setGameMode(@PathVariable("mode") String mode) {
		return ResponseEntity.ok().body(playerService.changeMode(mode));
	}
}
