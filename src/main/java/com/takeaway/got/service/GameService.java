package com.takeaway.got.service;

import com.takeaway.got.dto.CurrentPlayedDto;

public interface GameService {

    String startGame(CurrentPlayedDto currentPlayedDto);
    String playTurn(CurrentPlayedDto currentPlayedDto);
}
