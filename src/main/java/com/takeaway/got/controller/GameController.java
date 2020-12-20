package com.takeaway.got.controller;

import com.takeaway.got.dto.CurrentPlayedDto;
import com.takeaway.got.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("game")
public class GameController {

    @Autowired
    GameService gameService;

    @RequestMapping(method = RequestMethod.POST, value="/new", consumes = "application/json")
    public String newGame(@RequestBody CurrentPlayedDto currentPlayedDto) {
        return gameService.startGame(currentPlayedDto);
    }

    @RequestMapping(method = RequestMethod.POST, value="/play", consumes = "application/json")
    public String playTurn(@RequestBody CurrentPlayedDto currentPlayedDto) {
        return gameService.playManual(currentPlayedDto);
    }
}
