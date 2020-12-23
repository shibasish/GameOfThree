package com.takeaway.got.controller;

import com.takeaway.got.dto.CurrentPlayedDto;
import com.takeaway.got.dto.ResponseDto;
import com.takeaway.got.service.GameService;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    GameService gameService;

    @RequestMapping(method = RequestMethod.POST, value="/new", consumes = "application/json")
    public ResponseEntity<ResponseDto> newGame(@RequestBody CurrentPlayedDto currentPlayedDto) {
        return ResponseEntity.ok().body(gameService.startGame(currentPlayedDto));
    }

    @RequestMapping(method = RequestMethod.POST, value="/play", consumes = "application/json")
//    @ApiOperation( value = "Play turns for manual mode",
//            notes = "Provide value for toPlayer, fromPlayer, gameId and number",
//            response = String.class)
    public ResponseEntity<ResponseDto> playTurn(@RequestBody CurrentPlayedDto currentPlayedDto) {
        return ResponseEntity.ok().body(gameService.playManual(currentPlayedDto));
    }
}
